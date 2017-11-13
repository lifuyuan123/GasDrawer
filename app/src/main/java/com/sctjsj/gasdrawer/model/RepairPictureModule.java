package com.sctjsj.gasdrawer.model;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by liuha on 2017/5/26.
 */

public class RepairPictureModule {
    private IHttpService service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
    private RepairPictureModuleCallback callback;
    private Map<String,String> body;

    public RepairPictureModule(RepairPictureModuleCallback callback) {
        this.callback = callback;
    }

    public void UpImg(File file){

        service.uploadFile(null, NetAddress.GAS_UPIMG, file, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("-----",result.toString());
                try {
                    JSONObject obj=new JSONObject(result);
                    if(obj.getBoolean("result")){
                        //上传成功
                        JSONArray arr=new JSONArray(obj.getString("resultData"));
                        for (int i = 0; i <arr.length() ; i++) {
                            JSONObject data=arr.getJSONObject(i);
                            String ImgId=data.getInt("acyId")+"";
                            callback.Succeed(ImgId);
                        }
                    }else {
                            callback.Defeat();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("RepairJSONException",e.toString());
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

    public interface RepairPictureModuleCallback{
        public void Succeed(String imgId);
        public void  Defeat();
    }

}
