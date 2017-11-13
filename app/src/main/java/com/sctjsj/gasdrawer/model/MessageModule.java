package com.sctjsj.gasdrawer.model;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.entity.javaBean.MessageBean;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuha on 2017/5/23.
 * 消息界面的module
 */

public class MessageModule  {
    private IHttpService service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
    private List<MessageBean> data=new ArrayList<>();
    private Map<String,String> body=new HashMap<>();
    private MessageModuleCallBack callBack;
    private Context mContext;

    public MessageModule(Context mContext, MessageModuleCallBack callBack) {
        this.mContext = mContext;
        this.callBack = callBack;
    }

    //获取消息数据的网络请求
    public void getMessageData(){
        body.put("ctype","notice");
        body.put("cond","{type:2,is_publish:1}");
        body.put("orderby","publish_time desc");
        service.doCommonPost(null, NetAddress.GAS_MESSAGE, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                //请求成功
                pullMessageData(result);
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

    //解析message数据
    private void pullMessageData(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            if(obj.getBoolean("result")){
                JSONArray arr=obj.getJSONArray("resultList");
                for (int i = 0; i <arr.length() ; i++) {
                    JSONObject objMsg=arr.getJSONObject(i);
                    MessageBean bean=new MessageBean();
                    bean.setPublishTime(objMsg.getString("publishTime"));
                    bean.setContent(objMsg.getString("content"));
                    bean.setTitle(objMsg.getString("title"));
                    bean.setId(objMsg.getInt("id"));
                    data.add(bean);
                }
                callBack.succeed(data);
            }else{
                Toast.makeText(mContext,obj.getString("msg"),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //回掉数据的接口
    public interface MessageModuleCallBack{
        public void succeed(List data);
    }

}
