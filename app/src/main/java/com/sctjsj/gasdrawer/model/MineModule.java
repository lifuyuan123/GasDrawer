package com.sctjsj.gasdrawer.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.WorkerBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuha on 2017/5/25.
 */

public class MineModule {
    private IHttpService service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
    private Map<String,String> body;
    private MineModuleCallBack callback;
    private Context mContext;
    private int id= (int) SPFUtil.get(Tag.TAG_ID,0);

    public MineModule( MineModuleCallBack callback,Context mContext) {
        this.callback = callback;
        this.mContext=mContext;
    }

    //获取用户信息
    public void getWorkerMsg(){
        //if(0!=(int)SPFUtil.get("id",0)){
            //说明用户已经登陆
            body=new HashMap<>();
            body.put("ctype","user");
            body.put("id",id+"");
            body.put("jf","head");

            service.doCommonPost(null, NetAddress.GAS_USERMSG, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {

                    Log.e("MineonSuccess",result.toString());
                    pullJson(result);
                }

                @Override
                public void onError(Throwable ex) {
                    Log.e("MineononError",ex.toString());
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {
                    Log.e("MineononCancelled","onCancelled");
                }

                @Override
                public void onFinished() {
                    Log.e("MineononFinished","onFinished");
                }

                @Override
                public void onWaiting() {
                    Log.e("MineononWaiting","onWaiting");
                }

                @Override
                public void onStarted() {

                }

                @Override
                public void onLoading(long total, long current) {

                }
            });
        }




    //上传用户头像
    public void UpLoadedIcon(File file){
        service.uploadFile(null, NetAddress.GAS_UPIMG, file, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("-----",result.toString());
                try {
                    JSONObject obj=new JSONObject(result);
                    Log.e("-----",result.toString());
                    if(obj.getBoolean("result")){
                        //修改用户信息
                        String ImgId=obj.getString("resultData");
                        JSONArray arr=new JSONArray(ImgId);
                        for (int i = 0; i <arr.length() ; i++) {
                            JSONObject id=arr.getJSONObject(i);
                            int acyId=id.getInt("acyId");
                            ChangUserMsg(acyId+"");
                        }
                        Toast.makeText(mContext,"修改头像成功",Toast.LENGTH_SHORT).show();
                        callback.changIconSucceed();
                    }else {
                        Toast.makeText(mContext,"头像上传失败！",Toast.LENGTH_SHORT).show();
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




    //上传修改用户头像信息
    private void ChangUserMsg( String ImgId) {
        body=new HashMap<>();
        body.put("id",id+"");
        body.put("headId",ImgId);
        service.doCommonPost(null, NetAddress.GAS_CHANGEPAS, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d("head",result.toString());
                try {
                    JSONObject obj=new JSONObject(result);
                    if(obj.getBoolean("result")){
                        Toast.makeText(mContext, obj.getString("resultMsg"), Toast.LENGTH_SHORT).show();
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


    //}
    //解析数据
    private void pullJson(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            if(obj.getBoolean("result")){
                JSONObject data=obj.getJSONObject("data");
                WorkerBean bean=new WorkerBean();
                bean.setType(data.getInt("type"));
                bean.setId(data.getInt("id"));
                bean.setNickname(data.getString("nickname"));
                bean.setPhone(data.getString("phone"));
                bean.setSign(data.getString("sign"));
                bean.setUsername(data.getString("username"));
                if (data.has("head")){
                    JSONObject head=data.getJSONObject("head");
                    bean.setSmallUrl(head.getString("url"));
                }
                callback.getMineMsg(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MineModuleJSONException",e.toString());
        }
    }

    //退出登陆的方法
    public  void  LogOut(final Activity context, final int type){
        service.doCommonPost(null, NetAddress.GAS_LOGOUT, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("outonSuccess",result.toString());
                try {
                    JSONObject object=new JSONObject(result);
                    if(object.getBoolean("result")){
                        if(type==1){
                            Toast.makeText(mContext,"用户已注销",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext,"请重新登陆",Toast.LENGTH_SHORT).show();
                        }
                        //清除本地纪录
                        SPFUtil.clearAll();
                        context.finish();
                        ARouter.getInstance().build("/main/act/login").navigation();
                    }else {
                        SPFUtil.clearAll();
                        context.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("logout",e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("outonError",ex.toString());
                //清除本地纪录
                SPFUtil.clearAll();
                context.finish();

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
                Log.e("outonCancelled","onCancelled");
            }

            @Override
            public void onFinished() {
                Log.e("outonFinished","onFinished");
            }

            @Override
            public void onWaiting() {
                Log.e("outonWaiting","onWaiting");
            }

            @Override
            public void onStarted() {
                Log.e("outonStarted","onStarted");
            }

            @Override
            public void onLoading(long total, long current) {
                Log.e("outonLoading","onLoading");
            }
        });


    }

    public interface MineModuleCallBack{
        public void getMineMsg(WorkerBean bean);
        public void changIconSucceed();
    }

}
