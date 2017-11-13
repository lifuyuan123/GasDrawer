package com.sctjsj.gasdrawer.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.sctjsj.basemodule.base.HttpTask.XCacheCallback;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.application.MyApp;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.GroupBean;
import com.sctjsj.gasdrawer.entity.javaBean.CacheBean;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.RepairGasBean;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;
import com.sctjsj.gasdrawer.util.BluetoothService;
import com.sctjsj.gasdrawer.util.CacheUtils;
import com.sctjsj.gasdrawer.util.MyDbHelper;
import com.sctjsj.gasdrawer.util.PicFromPrintUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.sctjsj.gasdrawer.util.OtherConstant.STATE_CONNECTED;

@Route(path = "/main/act/RepairMessageActivity")
public class RepairMessageActivity extends BaseAppcompatActivity {

    @BindView(R.id.repair_message_back)
    ImageView repairMessageBack;
    @BindView(R.id.repair_message_btn)
    TextView repairMessageBtn;
    @BindView(R.id.repair_Usermessage)
    LinearLayout repairUsermessage;
    @BindView(R.id.repair_workMsg)
    LinearLayout repairWorkMsg;
    @BindView(R.id.repair_cause)
    LinearLayout repairCause;
    @BindView(R.id.repair_faultmsg)
    LinearLayout repairFaultmsg;
    @BindView(R.id.repair_gas)
    LinearLayout repairGas;
    @BindView(R.id.repair_other)
    LinearLayout repairOther;
    @BindView(R.id.repair_pic)
    LinearLayout repairPic;
    @Autowired(name = "data")
    WorkMessageBean bean;
    private BluetoothService service;
    private Bitmap bm;
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "qm.png";
    private List<GroupBean> list = new ArrayList<>();
    private List<List<ChildBean>> childlist = new ArrayList<>();
    private List<List<ServerChildBean>> serverChildlist = new ArrayList<>();
    private boolean flag = false;
    private List<ChildBean> childset = new ArrayList<>();
    private List<ServerChildBean> list1 = new ArrayList<>();
    private List<String> picId = new ArrayList<>();
    private List<String> picIdAfter = new ArrayList<>();
    private Map<String, String> datas = new HashMap<>();
    private Map<String, String> deta = new HashMap<>();
    private Map<String, String> stringMap = new HashMap<>();
    private List<RepairGasBean> gasBeen = new ArrayList<>();
    private int payment=-1;
    private int ispay=-1;
    private MyApp app;
    private MyDbHelper dbHelper;
    //      打印纸一行最大的字节
    private static final int LINE_BYTE_SIZE = 48;
    //      打印三列时，中间一列的中心线距离打印纸左侧的距离
    private static final int LEFT_LENGTH = 24;
    //     打印三列时，中间一列的中心线距离打印纸右侧的距离
    private static final int RIGHT_LENGTH = 24;
    //   打印三列时，第一列汉字最多显示几个文字
    private static final int LEFT_TEXT_MAX_LENGTH = 8;

    private int distype,distypeSer;
    private int length;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Intent intent=new Intent(RepairMessageActivity.this,PrintActivity.class);
                intent.putExtra("bean",bean);
                intent.putExtra("list1", (Serializable) list1);
                intent.putExtra("childset", (Serializable) childset);
                intent.putExtra("payment",payment);
                intent.putExtra("ispay",ispay);
                intent.putExtra("key",2);
                intent.putExtra("length",length);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        获取服务和材料清单数据
        getServerAndMaterialData();
        app = (MyApp) getApplicationContext();
        service = app.getserver();
        dbHelper = new MyDbHelper(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_repair_message;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.repair_message_back, R.id.repair_Usermessage, R.id.repair_message_btn,
            R.id.repair_workMsg, R.id.repair_cause, R.id.repair_faultmsg, R.id.repair_gas,
            R.id.repair_other, R.id.repair_pic, R.id.lin_server, R.id.lin_material,
            R.id.repair_autograph,R.id.repair_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.repair_Usermessage:
                ARouter.getInstance().build("/main/act/CustomerMsg").withObject("data", bean).navigation();
                break;
            case R.id.repair_message_back:
                finish();
                break;
            //打印
            case R.id.repair_message_btn:
                //打印的方法
//                autograph();
                if (bean != null&& childset.size() > 0 && bm != null &&
                        picId.size() > 0&&picIdAfter.size()>0&&payment!=-1&&ispay!=-1) {
                upLoadData();
                }
                break;
            case R.id.repair_workMsg:
                ARouter.getInstance().build("/main/act/RepairWork").withObject("data", bean).navigation();
                break;
            case R.id.repair_cause:
                ARouter.getInstance().build("/main/act/RepairCheck").navigation(this, 100);
                break;
            case R.id.repair_faultmsg:
                ARouter.getInstance().build("/main/act/RepairCase").navigation(this, 100);
                break;
            case R.id.repair_gas:
                ARouter.getInstance().build("/main/act/RepairGasBean").navigation(this, 100);
                break;
            case R.id.repair_other:
                ARouter.getInstance().build("/main/act/RepairOther").navigation(this, 100);
                break;
            case R.id.repair_pic:
                ARouter.getInstance().build("/main/act/RepairPicture").withObject("data", bean).navigation(this, 100);
                break;
            //服务
            case R.id.lin_server:
//                ARouter.getInstance().build("/main/act/AddServerActivity").navigation();
                Intent intent = new Intent(this, AddServerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) list);
                bundle.putSerializable("data", (Serializable) serverChildlist);
                intent.putExtra("key", bundle);
                startActivityForResult(intent, 100);

                break;
            //材料
            case R.id.lin_material:
//                ARouter.getInstance().build("/main/act/MaterialActivity").navigation();
                Intent mIntent = new Intent(this, MaterialActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("list", (Serializable) list);
                mBundle.putSerializable("data", (Serializable) childlist);
                mIntent.putExtra("key", mBundle);
                startActivityForResult(mIntent, 100);
                break;
            //签名
            case R.id.repair_autograph:
                startActivityForResult(new Intent(RepairMessageActivity.this, AutographActivity.class), 100);
                break;
            //支付方式
            case R.id.repair_pay:
                    ARouter.getInstance().build("/main/act/PayStyleActivity").navigation(this, 100);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheUtils.cleanAll();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            switch (resultCode) {
                //材料
                case 252:
                    Bundle data1 = data.getBundleExtra("data");
                    childset = (List<ChildBean>) data1.getSerializable("data");
                    distype=data.getIntExtra("distype",2);
                    Log.e("diatype",distype+"");
                    break;
                //服务
                case 251:
                    Bundle data2 = data.getBundleExtra("data");
                    list1 = (List<ServerChildBean>) data2.getSerializable("data");
                    distypeSer=data.getIntExtra("distype",2);
                    length=data.getIntExtra("materialall",0);
                    Log.e("diatype",distypeSer+"  length"+length);
                    break;
                //签名图片
                case 102:
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    bm = BitmapFactory.decodeFile(path, options);
                    break;
                //维修照片
                case 104:
                    Map<String,Object> map=new HashMap<>();
                    map= (Map<String, Object>) data.getSerializableExtra("key");
                    picId = (List<String>) map.get("picId");
                    picIdAfter= (List<String>) map.get("picIdAfter");
                    break;
                //其他维护事项
                case 121:
                    datas = (Map<String, String>) data.getSerializableExtra("key");
                    break;
//                故障与维修情况
                case 105:
                    deta = (Map<String, String>) data.getSerializableExtra("key");
                    break;
//                燃具维修
                case 106:
                    gasBeen = (List<RepairGasBean>) data.getSerializableExtra("key");
                    break;
//                现场查看故障原因
                case 107:
                    stringMap = (Map<String, String>) data.getSerializableExtra("key");
                    break;
                //缴费方式
                case 108:
                    payment= (int) data.getSerializableExtra("key");
                    ispay= (int) data.getSerializableExtra("keys");
                    break;
            }
        }

    }

    //获取服务和材料清单数据
    private void getServerAndMaterialData() {
        HttpServiceImpl server = new HttpServiceImpl();
        server.setUseCacheFlag(false);
        Map<String, String> pParameters = new HashMap<>();
        pParameters.put("ctype", "itemClass");
        pParameters.put("jf", "materials|serveritems");
        server.doCommonPostWithCache(new HashMap<String, String>(), NetAddress.CLASS_IFICATION, pParameters, 10, new XCacheCallback() {
            @Override
            public void onSuccess(String result) {
                if (result != null && result.length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("resultList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            GroupBean bean = new GroupBean();
                            List<ChildBean> childBeanList = new ArrayList<>();
                            List<ServerChildBean> serverlist = new ArrayList<>();
                            bean.setClassName(object.getString("className"));
                            bean.setId(object.getInt("id"));
                            bean.setType(object.getInt("type"));
                            bean.setTime(object.getString("insertTime"));
                            JSONArray jsonArray1 = object.getJSONArray("materials");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject object1 = jsonArray1.getJSONObject(j);
                                ChildBean bean1 = new ChildBean();
                                bean1.setMaterialName(object1.getString("materialName"));
                                bean1.setId(object1.getInt("id"));
                                bean1.setModel(object1.getString("model"));
                                bean1.setPurchasePrice(object1.getInt("purchasePrice"));
                                bean1.setRemarks(object1.getString("remarks"));
                                bean1.setSpecInfo(object1.getString("specInfo"));
                                bean1.setSellingPrice(object1.getInt("sellingPrice"));
                                bean1.setUnit(object1.getString("unit"));
                                childBeanList.add(bean1);
                            }
                            //服务子列表数据
                            JSONArray jsonArray2 = object.getJSONArray("serveritems");
                            for (int k = 0; k < jsonArray2.length(); k++) {
                                JSONObject o = jsonArray2.getJSONObject(k);
                                ServerChildBean b = new ServerChildBean();
                                b.setId(o.getInt("id"));
                                b.setPrice(o.getInt("price"));
                                b.setProName(o.getString("proName"));
                                b.setRemarks(o.getString("remarks"));
                                b.setUnit(o.getString("unit"));
                                serverlist.add(b);
                                Log.e("cccccobject", o.toString());
                            }
                            serverChildlist.add(serverlist);
                            childlist.add(childBeanList);
                            list.add(bean);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("aaaaa", "null");
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
            public boolean onCache(Object result) {
                return false;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }


    //上传操作------------------------------------------------

    //上传数据
    private void upLoadData() {
            showLoading(true,"上传中...");
            final Gson gson = new Gson();
            IHttpService service = (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
            final Map<String, String> body = new HashMap<>();
            if (!TextUtils.isEmpty(bean.getId() + "")) {
                body.put("orderId", bean.getId() + "");
                Log.e("body orderId", bean.getId() + "");
            }
            if(list1.size()>0){
            if (!TextUtils.isEmpty(getserverCarts())) {
                body.put("serverCarts", getserverCarts());
                Log.e("body serverCarts", getserverCarts());
            }
            }
            if (!TextUtils.isEmpty(getpic())) {
                body.put("piclist", getpic());
                Log.e("body piclist", getpic());
            }
            if (!TextUtils.isEmpty(gson.toJson(gasBeen))) {
                body.put("repairTool", gson.toJson(gasBeen));
                Log.e("body repairTool", gson.toJson(gasBeen));
            }
            if (!TextUtils.isEmpty(getmaterialCarts())) {
                body.put("materialCarts", getmaterialCarts());
                Log.e("body materialCarts", getmaterialCarts());
            }
            if (!TextUtils.isEmpty(datas.get("qbsy"))) {

                body.put("testAri", datas.get("qbsy"));
                Log.e("body testAri", datas.get("qbsy"));
            }
            if (!TextUtils.isEmpty(datas.get("other"))) {
                body.put("otherEvent", datas.get("other"));
                Log.e("body otherEvent", datas.get("other"));
            }
            if (!TextUtils.isEmpty(deta.get("securityDate"))) {
                body.put("securityDate", deta.get("securityDate"));
                Log.e("body securityDate", deta.get("securityDate"));
            }
            if (!TextUtils.isEmpty(deta.get("pressure"))) {
                body.put("pressure", deta.get("pressure"));
                Log.e("body pressure", deta.get("pressure"));
            }
            if (!TextUtils.isEmpty(deta.get("material"))) {
                body.put("material", deta.get("material"));
                Log.e("body material", deta.get("material"));
            }
            if (!TextUtils.isEmpty(deta.get("leakage"))) {
                body.put("leakage", deta.get("leakage"));
                Log.e("body leakage", deta.get("leakage"));
            }
            if (!TextUtils.isEmpty(stringMap.get("repairReson"))) {
                body.put("repairReson", stringMap.get("repairReson"));
                Log.e("body repairReson", stringMap.get("repairReson"));
            }

            if (!TextUtils.isEmpty(stringMap.get("isConform"))) {
                body.put("isConform", stringMap.get("isConform"));
                Log.e("body isConform", stringMap.get("isConform"));
            }
            if (!TextUtils.isEmpty(deta.get("installDate"))) {
                body.put("installDate", deta.get("installDate"));
                Log.e("body installDate", deta.get("installDate"));
            }
            if (!TextUtils.isEmpty(stringMap.get("gasType"))) {
                body.put("gasType", stringMap.get("gasType"));
                Log.e("body gasType", stringMap.get("gasType"));
            }
            if(payment!=-1){
                body.put("payment",String.valueOf(payment));
                Log.e("body payment",payment+"");
            }
            if(ispay!=-1){
                body.put("is_paysuccess",String .valueOf(ispay));
                Log.e("body ispay",ispay+"");
            }
            body.put("meter",String.valueOf(length));
            body.put("serverDiscount",String.valueOf(distypeSer));
            body.put("materialDiscount",String.valueOf(distype));

        LogUtil.e("body body",body.toString());
            service.doCommonPost(null, NetAddress.REPAIR, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {

                    Log.e("body result", result.toString());
                    if (result != null && result.length() > 0) {
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.getBoolean("result")) {
                                Toast.makeText(RepairMessageActivity.this, "上传成功。", Toast.LENGTH_SHORT).show();
                                Message message=Message.obtain();
                                message.what=1;
                                handler.sendMessage(message);
                            } else {
                                //上传失败
                                Toast.makeText(RepairMessageActivity.this, "上传失败。", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onError(Throwable ex) {
                    Log.e("result", ex.toString());
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {
                    Log.e("result", "onCancelled");
                }

                @Override
                public void onFinished() {
                    Log.e("result", "onFinished");
                    dismissLoading();
                }

                @Override
                public void onWaiting() {
                    Log.e("result", "result");
                }

                @Override
                public void onStarted() {
                    Log.e("result", "onStarted");
                }

                @Override
                public void onLoading(long total, long current) {
                    Log.e("result", "onLoading");
                }
            });
//        }
    }


    //拼接参数
    private String getserverCarts() {
        StringBuffer materialCarts = new StringBuffer();
        materialCarts.append("[");
        for (int i = 0; i < list1.size(); i++) {
            ServerChildBean bean = list1.get(i);
            materialCarts.append("{" + "count:" + "1" + "," + "price:" + bean.getPrice()+ "," + "totalPrice:" + bean.getEditext() + "," + "serveritem:" + "{" + "id:" + bean.getId() + "}");
            if (i == list1.size() - 1 || list1.size() == 1) {
                materialCarts.append("}]");
            } else {
                materialCarts.append("},");
            }
        }
        String data = materialCarts.toString();
        return data;
    }


    //拼接参数
    private String getmaterialCarts() {
        StringBuffer materialCarts = new StringBuffer();
        materialCarts.append("[");
        for (int i = 0; i < childset.size(); i++) {
            ChildBean bean = childset.get(i);
            materialCarts.append("{" + "count:" + bean.getCount() + "," + "price:" + bean.getUnit() + "," + "totalPrice:" + bean.getAll() + "," + "bnMaterialTbl:" + "{" + "id:" + bean.getId() + "}");
            if (i == childset.size() - 1 || childset.size() == 1) {
                materialCarts.append("}]");
            } else {
                materialCarts.append("},");
            }
        }
        String data = materialCarts.toString();
        return data;
    }

    private String getpic() {
        StringBuffer stringBuffer = new StringBuffer();
//        for (int i = 0; i < picId.size(); i++) {
//            if (picId.size() == 1) {
//                stringBuffer.append(picId.get(i));
//            }
//            if (picId.size() > 1) {
//                if (i == picId.size() - 1) {
//                    stringBuffer.append(picId.get(i));
//                } else {
//                    stringBuffer.append(picId.get(i) + ",");
//                }
//
//            }
//        }

        stringBuffer.append("[");
                for (int j = 0; j < picId.size(); j++) {
                    if(picId.size()==1||j==picId.size()-1){
                        stringBuffer.append("{"+"id"+":"+picId.get(j)+","+"type"+":"+1+"}");
                        if(picIdAfter.size()>0) {
                            stringBuffer.append(",");
                            if (j == picId.size() - 1) {
                                for (int i = 0; i < picIdAfter.size(); i++) {
                                    if (picIdAfter.size() == 1 || i == picIdAfter.size() - 1) {
                                        stringBuffer.append("{" + "id" + ":" + picIdAfter.get(i) + "," + "type" + ":" +2+ "}]");
                                    } else {
                                        stringBuffer.append("{" + "id" + ":" + picIdAfter.get(i) + "," + "type" + ":" +2+ "},");
                                    }
                                }
                            }
                            } else {
                                stringBuffer.append("]");
                            }

                    }else {
                        stringBuffer.append("{"+"id"+":"+picId.get(j)+","+"type"+":"+1+"},");
                    }
                }
        return stringBuffer.toString();
    }

}
