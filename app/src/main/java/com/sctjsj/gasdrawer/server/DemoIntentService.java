package com.sctjsj.gasdrawer.server;

import android.content.Context;
import android.util.Log;

import com.igexin.assist.MessageBean;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.sctjsj.gasdrawer.application.MyApp;
import com.sctjsj.gasdrawer.entity.javaBean.Message;
import com.sctjsj.gasdrawer.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lifuy on 2017/6/5.
 */

public class DemoIntentService extends GTIntentService {
    private  static int num;
    private MyApp app=new MyApp();

    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();
        String data=new String(payload);
        Log.e("msg",msg.getMessageId().toString()+" "+msg.getPayloadId().toString()+" "+msg.getTaskId().toString()+" "+new String(msg.getPayload()));
        num=app.getNum();
        num++;
        app.setNum(num);
        com.sctjsj.gasdrawer.entity.javaBean.MessageBean bean=new com.sctjsj.gasdrawer.entity.javaBean.MessageBean();
//        bean.setNum(num);
        bean.setContent(data);
        EventBus.getDefault().post(new MessageEvent(bean));
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }


}
