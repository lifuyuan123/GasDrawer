package com.sctjsj.gasdrawer.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.entity.javaBean.CustomerBean;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.ui.activity.PrintActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuha on 2017/5/24.
 * 安装作业单的界面的module
 */

public class InstalModule {

    private IHttpService service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
    private Map<String,String> body;
    private Context mContext;
    private InstalModuleCallBack callback;

    public InstalModule(Context mContext, InstalModuleCallBack callback) {
        this.mContext = mContext;
        this.callback = callback;
    }


    //获取用户信息
    public void getCustomerMsg(int id){
        body=new HashMap<>();
        body.put("ctype","workorder");
        body.put("id",""+id);
        service.doCommonPost(null, NetAddress.GAS_CUS, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                pullJson(result);
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
    //上传安装作业单
    public void upOrderMsg(String materialCarts,String serverCarts,String orderId,String sigPic,
    int length,int distype,int distypeSer,String imgIdPlan){
        body=new HashMap<>();
        body.put("materialCarts",materialCarts);
        body.put("serverCarts",serverCarts);
        body.put("orderId",orderId);
        body.put("sigPic",sigPic);
        body.put("installPic_id",imgIdPlan);
        body.put("meter",String.valueOf(length));
        body.put("serverDiscount",String.valueOf(distypeSer));
        body.put("materialDiscount",String.valueOf(distype));
        Log.e("updata--上传的数据",body.toString());
        service.doCommonPost(null, NetAddress.GAS_INSTAL, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("updata",result.toString());
                try {
                    JSONObject object=new JSONObject(result);
                    if(object.getBoolean("result")){
                        Toast.makeText(mContext, "上传成功,请到待上传查看工单！", Toast.LENGTH_SHORT).show();
                        callback.upSucceed();
                    }else {
                        callback.upFalse();
                        Toast.makeText(mContext,"上传失败"+object.getString("resultMsg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("updataJSONException",e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("updataonError",ex.toString());
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


    //解析用户信息
    private void pullJson(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            if(obj.getBoolean("result")){
                JSONObject bean=obj.getJSONObject("data");
                CustomerBean Cusbean=new CustomerBean();
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
                callback.getData(Cusbean);
            }else {
                Toast.makeText(mContext,obj.getString("msg"),Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface InstalModuleCallBack{
        public void getData(CustomerBean data);
        public void upSucceed();
        public void upFalse();

    }
}
