package com.sctjsj.gasdrawer.model;

/**
 * Created by liuha on 2017/5/22.
 */

import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.ImageBean;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *待办页面的module
 */
public class BaclLogModule {
    private IHttpService service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
   private List<WorkMessageBean> datas;
    private DataCallBack callBack;
    private int id=(int)SPFUtil.get(Tag.TAG_ID,0);

    //请求待施工数据
    public void getWorkSGList(DataCallBack callBack){
        this.callBack=callBack;
            HashMap<String,String> body=new HashMap<>();
            body.put("ctype","workorder");
            //拼接字符串（按Id）
            String cond="{operator:{id:"+id+"},orderStatus:3,isDelete:1}";
            body.put("cond",cond);
            body.put("orderby","install_time asc");
            service.doCommonPost(new HashMap<String, String>(), NetAddress.BACKLOG_CON, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.e("getWorkSGList",result.toString());
                    getData(result);
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
    public void getWorkSCList(DataCallBack callBack){
        this.callBack=callBack;
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","workorder");
        String cond="{operator:{id:"+id+"},orderStatus:1,isDelete:1}";
        body.put("cond",cond);
        body.put("orderby","install_time asc");
        service.doCommonPost(new HashMap<String, String>(), NetAddress.BACKLOG_SC, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                getData(result);
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


    //待维修的订单数据
    public void getRepairData(DataCallBack callBack){
        this.callBack=callBack;
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","workorder");
        String cond="{operator:{id:"+id+"},orderStatus:6,isDelete:1}";
        body.put("cond",cond);
        body.put("orderby","install_time asc");
        service.doCommonPost(null, NetAddress.BACKLOG_SC, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("onSuccess",result.toString());
                getDataMore(result);
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



    //待审核
    public void getAuditData(DataCallBack callBack){
        this.callBack=callBack;
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","workorder");
        String cond="{operator:{id:"+id+"},orderStatus:4,isDelete:1}";
        body.put("cond",cond);
        body.put("pageSize","30");
        body.put("orderby","install_time asc");
        body.put("jf","waccess|materialCarts|serverCarts|reparis|creator|operator|sig_pic|repairList|extorder|");
        service.doCommonPost(null, NetAddress.BACKLOG_SC, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("AuditData",result.toString());
                getDataAudit(result);
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

    //解析待审核的数据
    private void getDataAudit(String result) {
        Log.e("ssss",result.toString());
        try {
            JSONObject objs=new JSONObject(result);
            if(objs.getBoolean("result")){
                JSONArray arrs=objs.getJSONArray("resultList");
                datas=new ArrayList<>();
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
                    bean.setPayTime(beabObject.getString("payTime"));
                    bean.setTel(beabObject.getString("tel"));
                    bean.setTotalprice(beabObject.getInt("totalprice"));
                    bean.setHouseId(beabObject.getString("houseId"));
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
                callBack.getData(datas);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("------",e.toString());
        }
    }


    //解析数据
    private void getData(String result) {
        Log.e("待上传",result.toString());
        try {
            JSONObject obj=new JSONObject(result);
            JSONArray arr=obj.getJSONArray("resultList");
            datas=new ArrayList<>();
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
            callBack.getData(datas);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("待上传---",e.toString());
        }
    }

    //解析数据
    private void getDataMore(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            Log.e("11111",result.toString());
            JSONArray arr=obj.getJSONArray("resultList");
            datas=new ArrayList<>();
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

                datas.add(bean);
            }
            callBack.getData(datas);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("11111ogaaaaa",e.toString());
        }
    }



    public interface DataCallBack{
        public void getData(List data);
    }


}
