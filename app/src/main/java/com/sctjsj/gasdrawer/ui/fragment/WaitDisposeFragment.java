package com.sctjsj.gasdrawer.ui.fragment;

import android.content.Context;
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
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.bluetooth.BluetoothService;
import com.sctjsj.basemodule.base.ui.frg.BaseFragment;
import com.sctjsj.basemodule.base.ui.widget.LoadingDialog;
import com.sctjsj.basemodule.base.util.LogUtil;
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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import q.rorbin.qrefreshlayout.QRefreshLayout;
import q.rorbin.qrefreshlayout.RefreshHandler;

import static com.sctjsj.gasdrawer.util.OtherConstant.STATE_CONNECTED;

/**
 * Created by liuha on 2017/5/22.
 * 待处理的fragment（待维修，待安装）
 */

public class WaitDisposeFragment extends Fragment {
    private  int USERTYPE=-1;
    private ListView mListView;
    private EditText mEditText;
    private BaclLogModule module;
    private BackLogAdapter adapter;
    private BackLogAdapter adapterR;
    private QRefreshLayout qRefreshLayout;
    private View mView;
    private LoadingDialog dialog;
    private int id=(int)SPFUtil.get(Tag.TAG_ID,0);
    private List<WorkMessageBean> datas=new ArrayList<>();//usertype=1
    private List<WorkMessageBean> datasR=new ArrayList<>();//usertype=2
    private IHttpService service;
    private int pageIndex=1;
    private int Index=1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                adapter.notifyDataSetChanged();
                mListView.setEnabled(true);
            }else if(msg.what==2){
                adapterR.notifyDataSetChanged();
                mListView.setEnabled(true);
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mView=inflater.inflate(R.layout.backlog_viewpager_fragment,null);
        service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
        Bundle arguments = getArguments();
        if(null!=arguments){
            USERTYPE=arguments.getInt("key");
        }
        initView();
//        initData();
        return mView;
    }

    private void initView() {
        mListView= (ListView) mView.findViewById(R.id.backlog_ViewPager_listView);
        mEditText= (EditText) mView.findViewById(R.id.backlog_ViewPager_Edt);
        qRefreshLayout= (QRefreshLayout) mView.findViewById(R.id.backlog_ViewPager_QR);
        adapter=new BackLogAdapter(getActivity(),datas);
        adapterR=new BackLogAdapter(getActivity(),datasR);
        switch (USERTYPE){
            case 1:
                mListView.setAdapter(adapter);
                break;
            case 2:
                mListView.setAdapter(adapterR);
                LogUtil.e("维修   adf");
                break;
        }
        setListeners();
    }

    private void initData() {
//        module=new BaclLogModule();
        switch (USERTYPE){
            case 1://安装
//                module.getWorkSGList(new BaclLogModule.DataCallBack() {
//                    @Override
//                    public void getData(List data) {
//                        Log.e("待上传  anzhuang",data.toString());
//                        adapter=new BackLogAdapter(getActivity(),data);
//                        mListView.setAdapter(adapter);
//                        qRefreshLayout.refreshComplete();
//                        dismissLoading();
//                    }
//                });
                getWorkSGList();

                break;
            case 2://维修
//                module.getRepairData(new BaclLogModule.DataCallBack() {
//                    @Override
//                    public void getData(List data) {
//                        Log.e("待上传  weixiu",data.toString());
//                        adapter=new BackLogAdapter(getActivity(),data);
//                        mListView.setAdapter(adapter);
//                        qRefreshLayout.refreshComplete();
//                        dismissLoading();
//                    }
//                });
                getRepairData();
                break;
        }
    }

    private void initDataMore() {
        switch (USERTYPE){
            case 1://安装
                getWorkSGListMore();
                break;
            case 2://维修
                getRepairDataMore();
                break;
        }
    }


    private void setListeners() {
        qRefreshLayout.setLoadMoreEnable(true);
        qRefreshLayout.setRefreshHandler(new RefreshHandler() {
            @Override
            public void onRefresh(QRefreshLayout refresh) {
                initData();
                mListView.setEnabled(false);
            }

            @Override
            public void onLoadMore(QRefreshLayout refresh) {
                initDataMore();
                mListView.setEnabled(false);
            }


        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              switch (USERTYPE){
                  case 1:
                      WorkMessageBean bean=(WorkMessageBean) adapter.getItem(position);
                      ARouter.getInstance().build("/user/main/act/instalActivity").withObject("data",bean).navigation();
                      break;
                  case 2:
                      WorkMessageBean beanR=(WorkMessageBean) adapterR.getItem(position);
                      ARouter.getInstance().build("/main/act/RepairMessageActivity").withObject("data",beanR).navigation();
                      break;
              }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading(true,"加载中...");
        initData();
    }

    //显示加载dialog
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
    //关闭加载 dialog
    public void dismissLoading(){
        if(dialog!=null){
            dialog.dismiss();
        }
    }

    //请求待施工数据   usertype=1
    public void getWorkSGList(){
        datas.clear();
        pageIndex=1;
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","workorder");
        //拼接字符串（按Id）
        String cond="{operator:{id:"+id+"},orderStatus:3,isDelete:1}";
        body.put("cond",cond);
        body.put("pageIndex",pageIndex+"");
        body.put("orderby","install_time desc");
        service.doCommonPost(new HashMap<String, String>(), NetAddress.BACKLOG_CON, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getWorkSGList",result.toString());
                Log.e("安装",result.toString());
                try {
                    JSONObject obj=new JSONObject(result);
                    if(obj.getBoolean("result")){
                        pageIndex++;
                    JSONArray arr=obj.getJSONArray("resultList");
                    for (int i = 0; i <arr.length() ; i++) {
                        JSONObject objs=arr.getJSONObject(i);
                        WorkMessageBean bean=new WorkMessageBean();
                        bean.setAddress(objs.getString("address"));
                        bean.setCAENNumber(objs.getString("ceaNo"));
                        bean.setClientName(objs.getString("clientName"));
                        if(objs.has("departName")){
                            bean.setDepartName(objs.getString("departName"));
                        }
                        bean.setId(objs.getInt("id"));
                        bean.setInsertTime(objs.getString("insertTime"));
                        bean.setInstallTime(objs.getString("installTime"));
                        bean.setIsDelete(objs.getInt("isDelete"));
                        bean.setOrderNo(objs.getString("orderNo"));
                        bean.setOrderStatus(objs.getInt("orderStatus"));
                        bean.setOrdertype(objs.getInt("orderType"));
                        if(objs.has("payTime")) {
                            bean.setPayTime(objs.getString("payTime"));
                        }
                        bean.setTel(objs.getString("tel"));
                        bean.setTotalprice(objs.getInt("totalprice"));
                        if(objs.has("houseId")){
                            bean.setHouseId(objs.getString("houseId"));
                        }
                        if(objs.has("houseName")){
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
                Log.e("getWorkSGList",ex.toString());
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

    //请求待施工数据   usertype=1
    public void getWorkSGListMore(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","workorder");
        //拼接字符串（按Id）
        String cond="{operator:{id:"+id+"},orderStatus:3,isDelete:1}";
        body.put("cond",cond);
        body.put("pageIndex",pageIndex+"");
        body.put("orderby","install_time desc");
        service.doCommonPost(new HashMap<String, String>(), NetAddress.BACKLOG_CON, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getWorkSGList",result.toString());
                Log.e("安装_m",result.toString());
                try {
                    JSONObject obj=new JSONObject(result);
                    if(obj.getBoolean("result")){
                        pageIndex++;
                        JSONArray arr=obj.getJSONArray("resultList");
                        for (int i = 0; i <arr.length() ; i++) {
                            JSONObject objs=arr.getJSONObject(i);
                            WorkMessageBean bean=new WorkMessageBean();
                            bean.setAddress(objs.getString("address"));
                            bean.setCAENNumber(objs.getString("ceaNo"));
                            bean.setClientName(objs.getString("clientName"));
                            if(objs.has("departName")){
                                bean.setDepartName(objs.getString("departName"));
                            }
                            bean.setId(objs.getInt("id"));
                            bean.setInsertTime(objs.getString("insertTime"));
                            bean.setInstallTime(objs.getString("installTime"));
                            bean.setIsDelete(objs.getInt("isDelete"));
                            bean.setOrderNo(objs.getString("orderNo"));
                            bean.setOrderStatus(objs.getInt("orderStatus"));
                            bean.setOrdertype(objs.getInt("orderType"));
                            if(objs.has("payTime")) {
                                bean.setPayTime(objs.getString("payTime"));
                            }
                            bean.setTel(objs.getString("tel"));
                            bean.setTotalprice(objs.getInt("totalprice"));
                            if(objs.has("houseId")){
                                bean.setHouseId(objs.getString("houseId"));
                            }
                            if(objs.has("houseName")){
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
                Log.e("getWorkSGList",ex.toString());
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



    //待维修的订单数据 nusertype=2
    public void getRepairData(){
        Index=1;
        datasR.clear();
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","workorder");
        String cond="{operator:{id:"+id+"},orderStatus:6,isDelete:1}";
        body.put("cond",cond);
        body.put("pageIndex",Index+"");
        body.put("orderby","install_time desc");
        service.doCommonPost(null, NetAddress.BACKLOG_SC, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("维修",result.toString());
                try {
                    JSONObject obj=new JSONObject(result);
                    Log.e("11111",result.toString());
                    if(obj.getBoolean("result")){
                        Index++;
                    JSONArray arr=obj.getJSONArray("resultList");
                    for (int i = 0; i <arr.length() ; i++) {
                        JSONObject objs=arr.getJSONObject(i);
                        WorkMessageBean bean=new WorkMessageBean();
                        bean.setAddress(objs.getString("address"));
                        bean.setCAENNumber(objs.getString("ceaNo"));
                        bean.setClientName(objs.getString("clientName"));
                        if(objs.has("departName")){
                            bean.setDepartName(objs.getString("departName"));
                        }
                        bean.setId(objs.getInt("id"));
                        bean.setInsertTime(objs.getString("insertTime"));
                        bean.setInstallTime(objs.getString("installTime"));
                        bean.setIsDelete(objs.getInt("isDelete"));
                        bean.setOrderNo(objs.getString("orderNo"));
                        bean.setOrderStatus(objs.getInt("orderStatus"));
                        bean.setOrdertype(objs.getInt("orderType"));
                        if(objs.has("payTime")){
                            bean.setPayTime(objs.getString("payTime"));
                        }
                        bean.setTel(objs.getString("tel"));
                        if(objs.has("totalprice")){
                            bean.setTotalprice(objs.getInt("totalprice"));
                        }
                        bean.setHouseId(objs.getString("houseId"));
                        bean.setHouseName(objs.getString("houseName"));
                        bean.setAcceptedNumber(objs.getString("accNumber"));
                        if(objs.has("repairName")){
                            bean.setApplianceName(objs.getString("repairName"));
                        }
                        if(objs.has("repairReason")){
                            bean.setRepairReason(objs.getString("repairReason"));
                        }
                        datasR.add(bean);
                    }
                        Message m=Message.obtain();
                        m.what=2;
                        handler.sendMessage(m);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("11111ogaaaaa",e.toString());
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

    //待维修的订单数据 nusertype=2
    public void getRepairDataMore(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","workorder");
        String cond="{operator:{id:"+id+"},orderStatus:6,isDelete:1}";
        body.put("cond",cond);
        body.put("pageIndex",Index+"");
        body.put("orderby","install_time desc");
        service.doCommonPost(null, NetAddress.BACKLOG_SC, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("维修_m",result.toString());
                try {
                    JSONObject obj=new JSONObject(result);
                    Log.e("11111",result.toString());
                    if(obj.getBoolean("result")){
                        Index++;
                        JSONArray arr=obj.getJSONArray("resultList");
                        for (int i = 0; i <arr.length() ; i++) {
                            JSONObject objs=arr.getJSONObject(i);
                            WorkMessageBean bean=new WorkMessageBean();
                            bean.setAddress(objs.getString("address"));
                            bean.setCAENNumber(objs.getString("ceaNo"));
                            bean.setClientName(objs.getString("clientName"));
                            if(objs.has("departName")){
                                bean.setDepartName(objs.getString("departName"));
                            }
                            bean.setId(objs.getInt("id"));
                            bean.setInsertTime(objs.getString("insertTime"));
                            bean.setInstallTime(objs.getString("installTime"));
                            bean.setIsDelete(objs.getInt("isDelete"));
                            bean.setOrderNo(objs.getString("orderNo"));
                            bean.setOrderStatus(objs.getInt("orderStatus"));
                            bean.setOrdertype(objs.getInt("orderType"));
                            if(objs.has("payTime")){
                                bean.setPayTime(objs.getString("payTime"));
                            }
                            bean.setTel(objs.getString("tel"));
                            if(objs.has("totalprice")){
                                bean.setTotalprice(objs.getInt("totalprice"));
                            }
                            bean.setHouseId(objs.getString("houseId"));
                            bean.setHouseName(objs.getString("houseName"));
                            bean.setAcceptedNumber(objs.getString("accNumber"));
                            if(objs.has("repairName")){
                                bean.setApplianceName(objs.getString("repairName"));
                            }
                            if(objs.has("repairReason")){
                                bean.setRepairReason(objs.getString("repairReason"));
                            }
                            datasR.add(bean);
                        }
                        Message m=Message.obtain();
                        m.what=2;
                        handler.sendMessage(m);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("11111ogaaaaa",e.toString());
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
