package com.sctjsj.gasdrawer.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuha on 2017/6/1.
 */

public class AuditModule {
    private Context  mContext;
    private IHttpService service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
    private Map<String,String> body;
    private AuditModuleCallBAck callBAck;

    public AuditModule(Context mContext, AuditModuleCallBAck callBAck) {
        this.mContext = mContext;
        this.callBAck = callBAck;
    }

    public void pass(String orderId){
        body=new HashMap<>();
        body.put("orderId",orderId);
        body.put("state","6");
        service.doCommonPost(null, NetAddress.GAS_AUDIT, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("-----",result.toString());
                try {
                    JSONObject object=new JSONObject(result);
                    if(object.getBoolean("result")){
                       callBAck.succeed();
                    }else {
                        callBAck.defult();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    public void uuPass(String orderId, final String resion){
        body=new HashMap<>();
        body.put("orderId",orderId);
        body.put("state","7");
        body.put("resion",resion);
        service.doCommonPost(null, NetAddress.GAS_AUDIT, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("-----",result.toString());
                try {
                    JSONObject object=new JSONObject(result);
                    if(object.getBoolean("result")){
                        callBAck.succeed();
                    }else {
                        callBAck.defult();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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


  public   interface AuditModuleCallBAck{
        public void succeed();
        public void defult();

    }


}
