package com.sctjsj.gasdrawer.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.frg.BaseFragment;
import com.sctjsj.basemodule.base.ui.widget.LoadingDialog;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;
import com.sctjsj.gasdrawer.model.BaclLogModule;
import com.sctjsj.gasdrawer.presenter.adapter.BackLogAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import q.rorbin.qrefreshlayout.QRefreshLayout;
import q.rorbin.qrefreshlayout.RefreshHandler;

/**
 * Created by liuha on 2017/5/22.
 * 待上传的fragment
 */

public class WaitUploadFragment extends Fragment {
    private ListView mListView;
    private EditText mEditText;
    private BaclLogModule module;
    private BackLogAdapter adapter;
    private QRefreshLayout qRefreshLayout;
    private View mView;
    private List<WorkMessageBean> datas=new ArrayList<>();
    private int id=(int) SPFUtil.get(Tag.TAG_ID,0);
    private LoadingDialog dialog;
    private IHttpService service;
    private int pageIndex=1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                adapter.notifyDataSetChanged();
                mListView.setEnabled(true);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.backlog_viewpager_fragment,null);
        service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initView();
//        initData();
        return mView;
    }

    private void initData() {
        module=new BaclLogModule();
                module.getWorkSCList(new BaclLogModule.DataCallBack() {
                    @Override
                    public void getData(List data) {
                        adapter=new BackLogAdapter(getActivity(),data);
                        mListView.setAdapter(adapter);
                        qRefreshLayout.refreshComplete();
                        dismissLoading();
                    }
                });
    }


    protected void initView() {
        mListView= (ListView) mView.findViewById(R.id.backlog_ViewPager_listView);
        mEditText= (EditText) mView.findViewById(R.id.backlog_ViewPager_Edt);
        qRefreshLayout= (QRefreshLayout) mView.findViewById(R.id.backlog_ViewPager_QR);
        adapter=new BackLogAdapter(getActivity(),datas);
        mListView.setAdapter(adapter);
        setListeners();


    }

    private void setListeners() {
        qRefreshLayout.setLoadMoreEnable(true);
        qRefreshLayout.setRefreshHandler(new RefreshHandler() {
            @Override
            public void onRefresh(QRefreshLayout refresh) {
//                initData();
                getWorkSCList();
                mListView.setEnabled(false);
            }

            @Override
            public void onLoadMore(QRefreshLayout refresh) {
                getWorkSCListMore();
                mListView.setEnabled(false);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkMessageBean bean= (WorkMessageBean) adapter.getItem(position);
                ARouter.getInstance().build("/main/act/AffirmErectWorkActivity").withObject("bean",bean).navigation();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        showLoading(true,"加载中...");
        getWorkSCList();
    }

    /**
     * 显示加载dialog
     * @param str
     */
    public void showLoading(boolean b,String str){
        if(dialog==null){
            dialog=new LoadingDialog(this);
        }
        dialog.setCancelable(b);
        dialog.setLoadingStr(str);
        if(!dialog.isShowing()){
            dialog.show();
        }

    }

    /**
     * 关闭加载 dialog
     */
    public void dismissLoading(){
        if(dialog!=null){
            dialog.dismiss();
        }
    }



    //解析待上传的数据
    public void getWorkSCList(){
        datas.clear();
        pageIndex=1;
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","workorder");
        String cond="{operator:{id:"+id+"},orderStatus:1,isDelete:1}";
        body.put("cond",cond);
        body.put("pageIndex",pageIndex+"");
        body.put("orderby","install_time desc");
        service.doCommonPost(new HashMap<String, String>(), NetAddress.BACKLOG_SC, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("待上传",result.toString());
                try {
                    JSONObject obj=new JSONObject(result);
                    if(obj.getBoolean("result")) {
                        pageIndex++;
                        JSONArray arr = obj.getJSONArray("resultList");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject objs = arr.getJSONObject(i);
                            WorkMessageBean bean = new WorkMessageBean();
                            bean.setAddress(objs.getString("address"));
                            bean.setCAENNumber(objs.getString("ceaNo"));
                            bean.setClientName(objs.getString("clientName"));
                            if (objs.has("departName")) {
                                bean.setDepartName(objs.getString("departName"));
                            }
                            bean.setId(objs.getInt("id"));
                            bean.setInsertTime(objs.getString("insertTime"));
                            bean.setInstallTime(objs.getString("installTime"));
                            bean.setIsDelete(objs.getInt("isDelete"));
                            bean.setOrderNo(objs.getString("orderNo"));
                            bean.setOrderStatus(objs.getInt("orderStatus"));
                            bean.setOrdertype(objs.getInt("orderType"));
                            if (objs.has("payTime")) {
                                bean.setPayTime(objs.getString("payTime"));
                            }
                            bean.setTel(objs.getString("tel"));
                            bean.setTotalprice(objs.getInt("totalprice"));
                            if (objs.has("houseId")) {
                                bean.setHouseId(objs.getString("houseId"));
                            }
                            if (objs.has("houseName")) {
                                bean.setHouseName(objs.getString("houseName"));
                            }
                            bean.setAcceptedNumber(objs.getString("accNumber"));
                            datas.add(bean);
                        }
                        Message message=Message.obtain();
                        message.what=1;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("待上传---",e.toString());
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
                qRefreshLayout.refreshComplete();
                dismissLoading();
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


    //解析待上传的数据
    public void getWorkSCListMore(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","workorder");
        String cond="{operator:{id:"+id+"},orderStatus:1,isDelete:1}";
        body.put("cond",cond);
        body.put("pageIndex",pageIndex+"");
        body.put("orderby","install_time desc");
        service.doCommonPost(new HashMap<String, String>(), NetAddress.BACKLOG_SC, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("待上传",result.toString());
                try {
                    JSONObject obj=new JSONObject(result);
                    if(obj.getBoolean("result")) {
                        pageIndex++;
                        JSONArray arr = obj.getJSONArray("resultList");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject objs = arr.getJSONObject(i);
                            WorkMessageBean bean = new WorkMessageBean();
                            bean.setAddress(objs.getString("address"));
                            bean.setCAENNumber(objs.getString("ceaNo"));
                            bean.setClientName(objs.getString("clientName"));
                            if (objs.has("departName")) {
                                bean.setDepartName(objs.getString("departName"));
                            }
                            bean.setId(objs.getInt("id"));
                            bean.setInsertTime(objs.getString("insertTime"));
                            bean.setInstallTime(objs.getString("installTime"));
                            bean.setIsDelete(objs.getInt("isDelete"));
                            bean.setOrderNo(objs.getString("orderNo"));
                            bean.setOrderStatus(objs.getInt("orderStatus"));
                            bean.setOrdertype(objs.getInt("orderType"));
                            if (objs.has("payTime")) {
                                bean.setPayTime(objs.getString("payTime"));
                            }
                            bean.setTel(objs.getString("tel"));
                            bean.setTotalprice(objs.getInt("totalprice"));
                            if (objs.has("houseId")) {
                                bean.setHouseId(objs.getString("houseId"));
                            }
                            if (objs.has("houseName")) {
                                bean.setHouseName(objs.getString("houseName"));
                            }
                            bean.setAcceptedNumber(objs.getString("accNumber"));
                            datas.add(bean);
                        }
                        Message message=Message.obtain();
                        message.what=1;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("待上传---",e.toString());
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
             qRefreshLayout.LoadMoreComplete();
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
