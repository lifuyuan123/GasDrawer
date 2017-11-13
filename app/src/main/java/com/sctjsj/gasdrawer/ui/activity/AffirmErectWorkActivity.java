package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.CustomerBean;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;
import com.sctjsj.gasdrawer.model.AffirModule;
import com.sctjsj.gasdrawer.util.CacheUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

//安装作业确认单的页面
@Route(path = "/main/act/AffirmErectWorkActivity")
public class AffirmErectWorkActivity extends BaseAppcompatActivity implements AffirModule.AffirModuleCallBack {
    @Autowired(name = "bean")
    public WorkMessageBean bean;
    @BindView(R.id.erectWork_commit)
    TextView erectWorkCommit;

    private Map<String, String> map;
    private List<String> picID;
    private Map<String, Object> mapPic;
    private IHttpService servie;
    private Map<String, String> body;
    private AffirModule module ;
    private int conduit = -1;
    private List<ChildBean> childset=new ArrayList<>();
    private List<ServerChildBean> list1=new ArrayList<>();
    private CustomerBean Cusbean=new CustomerBean();
    private int length=0;
    private HttpServiceImpl service;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if(erectWorkCommit!=null)
                erectWorkCommit.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        module = new AffirModule(this, this);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        getCustomerMsg(bean.getId());
    }


    @Override
    public int initLayout() {
        return R.layout.activity_erectwork_affirm;
    }

    @Override
    public void reloadData() {
    }


    @OnClick({R.id.iv_back, R.id.rela_Customer_basic_information, R.id.rela_Dispatching_record, R.id.rela_program_advocacy, R.id.rela_Installation_position,
            R.id.rela_add_pic, R.id.erectWork_commit, R.id.rela_choose_conduit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                CacheUtils.cleanAll();
                break;
            //客户基本信息
            case R.id.rela_Customer_basic_information:
                ARouter.getInstance().build("/main/act/CustomerInformation").withObject("data", bean).navigation();
                break;
            //派工记录
            case R.id.rela_Dispatching_record:
                ARouter.getInstance().build("/main/act/DispatchRecord").withObject("data", bean).navigation();
                break;
            //规划说明
            case R.id.rela_program_advocacy:
                ARouter.getInstance().build("/main/act/ProgramAdvocacy").withObject("data", bean).navigation();
                break;
            //安装位置
            case R.id.rela_Installation_position:
                ARouter.getInstance().build("/main/act/Installationlocation").navigation(this, 200);
                break;
            //添加图片
            case R.id.rela_add_pic:
                ARouter.getInstance().build("/main/act/InstallRepairPicture").withObject("data", bean).navigation(this, 200);
                break;
            //提交
            case R.id.erectWork_commit:
                commitDate();
                break;
            //选择管道
            case R.id.rela_choose_conduit:
                ARouter.getInstance().build("/main/act/ChooseConduitActivity").navigation(this, 200);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            switch (resultCode) {
                case 205:
                    map = (Map<String, String>) data.getSerializableExtra("key");
                    break;
                case 104:
//                   picID= (List<String>) data.getSerializableExtra("key");
                    mapPic = (Map<String, Object>) data.getSerializableExtra("key");
                    break;
                //明封/暗埋
                case 106:
                    conduit = (int) data.getSerializableExtra("key");
                    LogUtil.e("conduit", conduit+"");
                    break;
            }
        }
    }

    //提交安装作业单
    private void commitDate() {
        if (null != mapPic && mapPic.size() > 0 && null != map && map.size() > 0 && conduit!=-1) {
            showLoading(true,"上传中...");
            module.UpData(map, mapPic, bean.getId(),conduit);
        } else {
            Toast.makeText(this, "请将数据填写完整!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void Succeed() {
        dismissLoading();
        Intent intent=new Intent(this,PrintActivity.class);
        intent.putExtra("Cusbean",Cusbean);
        intent.putExtra("list1", (Serializable) list1);
        intent.putExtra("childset", (Serializable) childset);
        intent.putExtra("key",1);
        intent.putExtra("length",length);
        LogUtil.e("updata--查询获取到数据","服务"+list1.toString()+"材料"+childset.toString()+"工程量数"+length);
        startActivity(intent);
        finish();
    }

    @Override
    public void Defult() {
        dismissLoading();
    }


    //获取用户信息
    public void getCustomerMsg(int id){
        body=new HashMap<>();
        body.put("ctype","workorder");
        body.put("id",""+id);
        body.put("jf","materialCarts|serverCarts|reparis|creator|operator|sig_pic|waccess|repairList|extorder|logList|bnMaterialTbl|serveritem");
        service.doCommonPost(null, NetAddress.GAS_CUS, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getdata",result.toString());
                try {
                    JSONObject obj=new JSONObject(result);
                    if(obj.getBoolean("result")){
                        JSONObject bean=obj.getJSONObject("data");
                        Cusbean.setAddress(bean.getString("address"));
                        Cusbean.setCAENo(bean.getString("ceaNo"));
                        Cusbean.setClientName(bean.getString("clientName"));
                        Cusbean.setDepartName(bean.getString("departName"));
                        Cusbean.setId(bean.getInt("id"));
                        Cusbean.setInsertTime(bean.getString("insertTime"));
                        Cusbean.setInstallTime(bean.getString("installTime"));
                        Cusbean.setOrderNo(bean.getString("orderNo"));
                        Cusbean.setOrderStatus(bean.getInt("orderStatus"));
                        Cusbean.setTel(bean.getString("tel"));
                        JSONArray array=bean.getJSONArray("materialCarts");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object=array.getJSONObject(i);
                            ChildBean childBean=new ChildBean();
                            if(object.has("count"))
                            childBean.setCount(object.getString("count"));
                            childBean.setId(object.getInt("id"));
                            childBean.setSellingPrice(object.getInt("price"));
                            childBean.setAll(object.getInt("totalPrice"));
                            childBean.setMaterialName(object.getJSONObject("bnMaterialTbl").getString("materialName"));
                            childset.add(childBean);
                        }

                        JSONArray arrays=bean.getJSONArray("serverCarts");
                        for (int i = 0; i <arrays.length() ; i++) {
                            JSONObject o=arrays.getJSONObject(i);
                            ServerChildBean serverBean=new ServerChildBean();
                            serverBean.setId(o.getInt("id"));
                            serverBean.setEditext(o.getInt("totalPrice")+"");
                            serverBean.setPrice(o.getInt("price"));
                            if(o.has("serveritem"))
                            serverBean.setProName(o.getJSONObject("serveritem").getString("proName"));
                            list1.add(serverBean);
                        }
                        if(bean.has("meter")) {
                            length = bean.getInt("meter");
                        }
                        Message m=Message.obtain();
                        m.what=1;
                        handler.sendMessage(m);
                    }else {
                        Toast.makeText(AffirmErectWorkActivity.this,obj.getString("msg"),Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("getdata_JSONException",e.toString());
                }

            }

            @Override
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }

}
