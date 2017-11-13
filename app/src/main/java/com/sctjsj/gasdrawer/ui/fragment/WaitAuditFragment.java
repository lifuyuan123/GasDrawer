package com.sctjsj.gasdrawer.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.frg.BaseFragment;
import com.sctjsj.basemodule.base.ui.widget.LoadingDialog;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.ImageBean;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
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
 * 待审核的fragment
 */

public class WaitAuditFragment extends BaseFragment {
    private ListView mListView;
    private EditText mEditText;
    private BaclLogModule module;
    private BackLogAdapter adapter;
    private QRefreshLayout layout;
    private HttpServiceImpl service;
    private int pageIndex=1;
    private LoadingDialog dialog;
    private List<WorkMessageBean> datas=new ArrayList<>();
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
    @Override
    protected int setLayoutResouceId() {
        return R.layout.backlog_viewpager_fragment;
    }

    @Override
    protected void initData(Bundle arguments) {
//       initDatas();

    }

    @Override
    protected void initView() {
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        mListView= (ListView) mRootView.findViewById(R.id.backlog_ViewPager_listView);
        mEditText= (EditText) mRootView.findViewById(R.id.backlog_ViewPager_Edt);
        layout= (QRefreshLayout) mRootView.findViewById(R.id.backlog_ViewPager_QR);
        adapter=new BackLogAdapter(getActivity(),datas);
        mListView.setAdapter(adapter);
        getAuditData();
        showLoading(true,"加载中...");
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkMessageBean bean=adapter.getData().get(position);
                ARouter.getInstance().build("/main/act/AuditActivity").withObject("data",bean).navigation();
            }
        });

        layout.setLoadMoreEnable(true);
        layout.setRefreshHandler(new RefreshHandler() {
            @Override
            public void onRefresh(QRefreshLayout refresh) {
                mListView.setEnabled(false);
                 getAuditData();
//                initDatas();
            }

            @Override
            public void onLoadMore(QRefreshLayout refresh) {
                mListView.setEnabled(false);
                getAuditDataMore();

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
//        getAuditData();
//        initDatas();
    }

    @Override
    protected void onLazyLoad() {

    }

    private void initDatas(){
        module=new BaclLogModule();
        module.getAuditData(new BaclLogModule.DataCallBack() {
            @Override
            public void getData(List data) {
                if(null!=data&&data.size()>0){
                    adapter=new BackLogAdapter(getActivity(),data);
                    mListView.setAdapter(adapter);
                    layout.refreshComplete();
                }
            }
        });
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



    //待审核
    public void getAuditData(){
        pageIndex=1;
        datas.clear();
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","workorder");
        String cond="{operator:{id:"+(int) SPFUtil.get(Tag.TAG_ID,0)+"},orderStatus:4,isDelete:1}";
        body.put("cond",cond);
        body.put("pageIndex",pageIndex+"");
        body.put("orderby","install_time desc");
        body.put("jf","waccess|materialCarts|serverCarts|reparis|creator|operator|sig_pic|repairList|extorder|");
        service.doCommonPost(null, NetAddress.BACKLOG_SC, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("AuditData",result.toString());
                Log.e("ssss",result.toString());
                try {
                    JSONObject objs=new JSONObject(result);
                    if(objs.getBoolean("result")){
                        pageIndex++;
                        JSONArray arrs=objs.getJSONArray("resultList");
                        for (int j = 0; j <arrs.length() ; j++) {
                            JSONObject beabObject=arrs.getJSONObject(j);
                            WorkMessageBean bean=new WorkMessageBean();
                            bean.setAddress(beabObject.getString("address"));
                            bean.setCAENNumber(beabObject.getString("ceaNo"));
                            bean.setClientName(beabObject.getString("clientName"));
                            bean.setDepartName(beabObject.getString("departName"));
                            bean.setId(beabObject.getInt("id"));
                            bean.setInsertTime(beabObject.getString("insertTime"));
                            bean.setInstallTime(beabObject.getString("installTime"));
                            bean.setIsDelete(beabObject.getInt("isDelete"));
                            bean.setOrderNo(beabObject.getString("orderNo"));
                            bean.setOrderStatus(beabObject.getInt("orderStatus"));
                            bean.setOrdertype(beabObject.getInt("orderType"));
                            if (beabObject.has("payTime"))
                            bean.setPayTime(beabObject.getString("payTime"));
                            bean.setTel(beabObject.getString("tel"));
                            bean.setTotalprice(beabObject.getInt("totalprice"));
                            if(beabObject.has("houseId"))
                            bean.setHouseId(beabObject.getString("houseId"));
                            if(beabObject.has("houseName"))
                            bean.setHouseName(beabObject.getString("houseName"));
                            bean.setAcceptedNumber(beabObject.getString("accNumber"));
                            JSONArray arr=beabObject.getJSONArray("materialCarts");
                            if(arr.length()>0){
                                List<ChildBean> data=new ArrayList<>();
                                for (int i = 0; i <arr.length() ; i++) {
                                    ChildBean child=new ChildBean();
                                    JSONObject material=arr.getJSONObject(i);
                                    child.setId(material.getInt("id"));
                                    if (material.has("count")){
                                        child.setCount(material.getInt("count")+"");
                                    }
                                    child.setUnit(material.getInt("price")+"");
                                    child.setEditext(material.getInt("totalPrice")+"");
                                    data.add(child);
                                }
                                bean.setMaterial(data);
                            }
                            JSONArray serverArr=beabObject.getJSONArray("serverCarts");
                            if(serverArr.length()>0){
                                List<ServerChildBean> data=new ArrayList<>();
                                for (int i = 0; i <serverArr.length() ; i++) {
                                    ServerChildBean ser=new ServerChildBean();
                                    JSONObject server=serverArr.getJSONObject(i);
                                    ser.setId(server.getInt("id"));
                                    if(server.has("count")){
                                        ser.setCount(server.getInt("count"));
                                    }
                                    ser.setPrice(server.getInt("price"));
                                    ser.setTotalPrice(server.getInt("totalPrice"));
                                    data.add(ser);
                                }
                                bean.setServerChildBeen(data);
                            }
                            JSONArray picArr=beabObject.getJSONArray("waccess");
                            if(picArr.length()>0){
                                List<ImageBean> picAddress=new ArrayList<>();
                                for (int i = 0; i <picArr.length() ; i++) {
                                    JSONObject PicObject=picArr.getJSONObject(i);
                                    ImageBean imageBean=new ImageBean();
                                    imageBean.setUrl(PicObject.getString("url"));
                                    if(PicObject.has("type")){
                                        imageBean.setType(PicObject.getInt("type"));
                                        switch (PicObject.getInt("type")){
                                            case 1:
                                                imageBean.setTitle("燃气灶");
                                                break;
                                            case 2:
                                                imageBean.setTitle("干衣机");
                                                break;
                                            case 3:
                                                imageBean.setTitle("热水器");
                                                break;
                                            case 4:
                                                imageBean.setTitle("两用灶");
                                                break;
                                        }
                                    }
                                    picAddress.add(imageBean);
                                }
                                bean.setPicAddress(picAddress);
                            }
                            datas.add(bean);
                        }
                        Message message=Message.obtain();
                        message.what=1;
                        handler.sendMessage(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("------",e.toString());
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
                layout.refreshComplete();
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

    //待审核
    public void getAuditDataMore(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","workorder");
        String cond="{operator:{id:"+(int) SPFUtil.get(Tag.TAG_ID,0)+"},orderStatus:4,isDelete:1}";
        body.put("cond",cond);
        body.put("pageIndex",pageIndex+"");
        body.put("orderby","install_time desc");
        body.put("jf","waccess|materialCarts|serverCarts|reparis|creator|operator|sig_pic|repairList|extorder|");
        service.doCommonPost(null, NetAddress.BACKLOG_SC, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("AuditData",result.toString());
                Log.e("ssss_more",result.toString());
                try {
                    JSONObject objs=new JSONObject(result);
                    if(objs.getBoolean("result")){
                        pageIndex++;
                        JSONArray arrs=objs.getJSONArray("resultList");
                        for (int j = 0; j <arrs.length() ; j++) {
                            JSONObject beabObject=arrs.getJSONObject(j);
                            WorkMessageBean bean=new WorkMessageBean();
                            bean.setAddress(beabObject.getString("address"));
                            bean.setCAENNumber(beabObject.getString("ceaNo"));
                            bean.setClientName(beabObject.getString("clientName"));
                            bean.setDepartName(beabObject.getString("departName"));
                            bean.setId(beabObject.getInt("id"));
                            bean.setInsertTime(beabObject.getString("insertTime"));
                            bean.setInstallTime(beabObject.getString("installTime"));
                            bean.setIsDelete(beabObject.getInt("isDelete"));
                            bean.setOrderNo(beabObject.getString("orderNo"));
                            bean.setOrderStatus(beabObject.getInt("orderStatus"));
                            bean.setOrdertype(beabObject.getInt("orderType"));
                            if (beabObject.has("payTime"))
                            bean.setPayTime(beabObject.getString("payTime"));
                            bean.setTel(beabObject.getString("tel"));
                            bean.setTotalprice(beabObject.getInt("totalprice"));
                            if(beabObject.has("houseId"))
                            bean.setHouseId(beabObject.getString("houseId"));
                            if(beabObject.has("houseName"))
                            bean.setHouseName(beabObject.getString("houseName"));
                            bean.setAcceptedNumber(beabObject.getString("accNumber"));
                            JSONArray arr=beabObject.getJSONArray("materialCarts");
                            if(arr.length()>0){
                                List<ChildBean> data=new ArrayList<>();
                                for (int i = 0; i <arr.length() ; i++) {
                                    ChildBean child=new ChildBean();
                                    JSONObject material=arr.getJSONObject(i);
                                    child.setId(material.getInt("id"));
                                    if (material.has("count")){
                                        child.setCount(material.getInt("count")+"");
                                    }
                                    child.setUnit(material.getInt("price")+"");
                                    child.setEditext(material.getInt("totalPrice")+"");
                                    data.add(child);
                                }
                                bean.setMaterial(data);
                            }
                            JSONArray serverArr=beabObject.getJSONArray("serverCarts");
                            if(serverArr.length()>0){
                                List<ServerChildBean> data=new ArrayList<>();
                                for (int i = 0; i <serverArr.length() ; i++) {
                                    ServerChildBean ser=new ServerChildBean();
                                    JSONObject server=serverArr.getJSONObject(i);
                                    ser.setId(server.getInt("id"));
                                    if(server.has("count")){
                                        ser.setCount(server.getInt("count"));
                                    }
                                    ser.setPrice(server.getInt("price"));
                                    ser.setTotalPrice(server.getInt("totalPrice"));
                                    data.add(ser);
                                }
                                bean.setServerChildBeen(data);
                            }
                            JSONArray picArr=beabObject.getJSONArray("waccess");
                            if(picArr.length()>0){
                                List<ImageBean> picAddress=new ArrayList<>();
                                for (int i = 0; i <picArr.length() ; i++) {
                                    JSONObject PicObject=picArr.getJSONObject(i);
                                    ImageBean imageBean=new ImageBean();
                                    imageBean.setUrl(PicObject.getString("url"));
                                    if(PicObject.has("type")){
                                        imageBean.setType(PicObject.getInt("type"));
                                        switch (PicObject.getInt("type")){
                                            case 1:
                                                imageBean.setTitle("燃气灶");
                                                break;
                                            case 2:
                                                imageBean.setTitle("干衣机");
                                                break;
                                            case 3:
                                                imageBean.setTitle("热水器");
                                                break;
                                            case 4:
                                                imageBean.setTitle("两用灶");
                                                break;
                                        }
                                    }
                                    picAddress.add(imageBean);
                                }
                                bean.setPicAddress(picAddress);
                            }
                            datas.add(bean);
                        }
                        Message message=Message.obtain();
                        message.what=1;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("------",e.toString());
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
                layout.LoadMoreComplete();
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
