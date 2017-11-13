package com.sctjsj.gasdrawer.model;

import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuha on 2017/5/25.
 * 修改密码
 */

public class ChangPasModule {
    private IHttpService service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
    private Map<String,String> body=new HashMap<>();
    private ChangPasModuleCallBack callback;

    public ChangPasModule(ChangPasModuleCallBack callback) {
        this.callback = callback;
    }

    public void  changPas(String oldpas, String newpas){
        int id = (int) SPFUtil.get(Tag.TAG_ID, 0);
        body.put("id",id+"");
        body.put("oldPasserd",oldpas);
        body.put("password",newpas);
        LogUtil.e("pass",body.toString());
        service.doCommonPost(null, NetAddress.GAS_CHANGEPAS, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                pullJson(result);
                Log.e("pass",result.toString());
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("pass","onError"+ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
                Log.e("pass","onCancelled");
            }

            @Override
            public void onFinished() {
                Log.e("pass","onFinished");
            }

            @Override
            public void onWaiting() {
                Log.e("pass","onWaiting");
            }

            @Override
            public void onStarted() {
                Log.e("pass","onStarted");
            }

            @Override
            public void onLoading(long total, long current) {
                Log.e("pass","onLoading");
            }
        });

    }

    private void pullJson(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            if(obj.getBoolean("result")){
                callback.changSucceed();
            }else {
                callback.changDefeat();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   public interface ChangPasModuleCallBack{
       public void changSucceed();
       public void changDefeat();

   }


}
