package com.sctjsj.gasdrawer.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.arouter.utils.TextUtils;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.ui.activity.AffirmErectWorkActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuha on 2017/6/2.
 */

public class AffirModule {
    private AffirModuleCallBack callBack;
    private Map<String,String> body;
    private Context context;
    private IHttpService service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();

    public AffirModule(AffirModuleCallBack callBack,Context context) {
        this.callBack = callBack;
        this.context=context;
    }

    public void UpData(Map<String,String> map, Map<String,Object> picID, int id,int conduit) {
        body = new HashMap<>();
        body.put("orderId", id + "");
        body.put("installWay",conduit+"");
        StringBuffer buffer = new StringBuffer();
//        for (int i = 0; i < picID.size(); i++) {
//            buffer.append(picID.get(i));
//            if (picID.size() - 1 != i) {
//                buffer.append(",");
//            }
//        }
        buffer.append("[");
        for (int i = 0; i < picID.size(); i++) {
            if(picID.containsKey("picId")){
                List<String> picId = (List<String>) picID.get("picId");
                for (int j = 0; j < picId.size(); j++) {
                    if(picId.size()==1||j==picId.size()-1){
                        buffer.append("{"+"id"+":"+picId.get(j)+","+"type"+":"+1+"}");
                    }else {
                        buffer.append("{"+"id"+":"+picId.get(j)+","+"type"+":"+1+"},");
                    }

                }
                if(picID.containsKey("picId1")||picID.containsKey("picId2")||picID.containsKey("picId3")){
                    buffer.append(",");
                }
            }
            if(picID.containsKey("picId1")){
                List<String> picId1 = (List<String>) picID.get("picId1");
                for (int j = 0; j < picId1.size(); j++) {
                    if(picId1.size()==1||picId1.size()-1==j){
                        buffer.append("{"+"id"+":"+picId1.get(j)+","+"type"+":"+4+"}");
                    }else {
                        buffer.append("{"+"id"+":"+picId1.get(j)+","+"type"+":"+4+"},");
                    }
                }
                if(picID.containsKey("picId2")||picID.containsKey("picId3")){
                    buffer.append(",");
                }
            }
            if(picID.containsKey("picId2")){
                List<String> picId2 = (List<String>) picID.get("picId2");
                for (int j = 0; j < picId2.size(); j++) {
                    if(picId2.size()==1||picId2.size()-1==j){
                        buffer.append("{"+"id"+":"+picId2.get(j)+","+"type"+":"+2+"}");
                    }else {
                        buffer.append("{"+"id"+":"+picId2.get(j)+","+"type"+":"+2+"},");
                    }

                }
                if(picID.containsKey("picId3")){
                    buffer.append(",");
                }
            }
            if(picID.containsKey("picId3")){
                List<String> picId3 = (List<String>) picID.get("picId3");
                for (int j = 0; j < picId3.size(); j++) {
                    if(picId3.size()==1||picId3.size()-1==j){
                        buffer.append("{"+"id"+":"+picId3.get(j)+","+"type"+":"+3+"}");
                    }else {
                        buffer.append("{"+"id"+":"+picId3.get(j)+","+"type"+":"+3+"},");
                    }

                }
            }
        }
        buffer.append("]");
        Log.e("11111",buffer.toString());
        body.put("piclist", buffer.toString());
        if (!TextUtils.isEmpty(map.get("installPlace"))) {
            body.put("installPlace", map.get("installPlace"));
        }
        if (!TextUtils.isEmpty(map.get("gasPlace"))) {
            body.put("gasPlace", map.get("gasPlace"));
        }
        if (!TextUtils.isEmpty(map.get("waterPlace"))) {
            body.put("waterPlace", map.get("waterPlace"));
        }
        if (!TextUtils.isEmpty(map.get("gasTwoPlace"))) {
            body.put("gasTwoPlace", map.get("gasTwoPlace"));
        }
        if (!TextUtils.isEmpty(map.get("clothePlace"))) {
            body.put("clothePlace", map.get("clothePlace"));
        }

        service.doCommonPost(null, NetAddress.GAS_ERE, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("----onSuccess", result.toString());
                try {
                    Log.e("onSuccess", result.toString());
                    JSONObject object = new JSONObject(result);
                    if (object.getBoolean("result")) {
                        //上传成功
                        Toast.makeText(context, "上传成功。", Toast.LENGTH_SHORT).show();
                        callBack.Succeed();
                    } else {
                        //上传失败
                        Toast.makeText(context, "上传失败。", Toast.LENGTH_SHORT).show();
                        callBack.Defult();
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

   public interface AffirModuleCallBack{
       public void Succeed();
       public void Defult();
   }

}
