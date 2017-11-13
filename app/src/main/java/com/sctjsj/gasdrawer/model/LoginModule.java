package com.sctjsj.gasdrawer.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.igexin.sdk.PushManager;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.setup.DeviceUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.application.MyApp;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuha on 2017/5/23.
 * 登陆的module
 */

public class LoginModule {
    private String DeviceId;
    private Context mContext;
    private IHttpService service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
    private Map<String,String> body=new HashMap<>();
    private LogincallBack back;

    public LoginModule(Context mContext,LogincallBack back) {
        this.mContext = mContext;
        this.back=back;
    }

    //登陆的方法
    public  void login(String name,String pas){
      //获取设备的deviceId
        DeviceId="ANDROID_"+DeviceUtil.getDeviceId(mContext);
        body.put("devId",DeviceId);
        body.put("userName",name);
        body.put("password",pas);
        body.put("cid", PushManager.getInstance().getClientid(mContext));
        service.doCommonPost(null, NetAddress.GAS_LOGIN, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("111111onSuccess",result.toString());
                pullJson(result);
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("111111onError",ex.toString());
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
    //解析JSON
    private void pullJson(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            if(obj.getBoolean("result")){
                //登陆成功
                SPFUtil.init(mContext);
                String resultData=obj.getString("info");
                JSONObject UserMsg=new JSONObject(resultData);
                SPFUtil.put(Tag.TAG_TOKEN,UserMsg.getString("token"));
                SPFUtil.put(Tag.TAG_TYPE,UserMsg.getInt("type"));
                SPFUtil.put(Tag.TAG_ID,UserMsg.getInt("id"));
                SPFUtil.put(Tag.TAG_NAME,UserMsg.getString("username"));
                NetAddress.USERTYPE=UserMsg.getInt("id");
                back.succeed();
            }else {
                 Toast.makeText(mContext,obj.getString("resultMsg"),Toast.LENGTH_SHORT).show();
            }
            //登陆失败

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("login",e.toString());
        }

    }

    public interface LogincallBack{
        public void succeed();
    }

}
