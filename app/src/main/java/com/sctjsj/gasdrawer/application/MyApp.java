package com.sctjsj.gasdrawer.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.igexin.sdk.PushManager;
import com.sctjsj.basemodule.base.SingletonManager;
import com.sctjsj.basemodule.base.service.SessionFlushService;
import com.sctjsj.basemodule.core.app.BaseApplication;
import com.sctjsj.basemodule.core.config.SystemConfig;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.gasdrawer.server.DemoIntentService;
import com.sctjsj.gasdrawer.server.DemoPushService;
import com.sctjsj.gasdrawer.ui.activity.MainActivity;
import com.sctjsj.gasdrawer.util.BluetoothService;
import com.sctjsj.gasdrawer.util.MyDbHelper;


/**
 * Created by Chris-Jason on 2016/10/31.
 */
public class MyApp extends BaseApplication {

    private int socketState=-1;
    private static int num=0;
    private BluetoothService bluetoothService;
    private onBTStateChangedListener stateChangedListener;

    @Override
    public void onCreate() {
        super.onCreate();
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(),DemoPushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);

    }

    public void setSocketState(int socketState) {
        this.socketState = socketState;
        if(stateChangedListener!=null){
            stateChangedListener.stateChanged(socketState);
        }

    }

    public interface onBTStateChangedListener{
        void stateChanged(int currentState);
    }

    public void setOnBTStateChangedListener(onBTStateChangedListener listener){
        this.stateChangedListener=listener;
    }

    public BluetoothService getblue(Handler mxHandler){
            BluetoothService service = new BluetoothService(this, mxHandler);
        this.bluetoothService=service;
            return service;
    }

    public BluetoothService getserver(){
        return bluetoothService;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
