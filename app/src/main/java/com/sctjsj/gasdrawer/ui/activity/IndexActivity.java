package com.sctjsj.gasdrawer.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;

import java.util.logging.LogRecord;

/***
 * 启动页
 */
public class IndexActivity extends BaseAppcompatActivity {
   Handler han=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           if(msg.what==1){
               finish();
               ARouter.getInstance().build("/user/act/index").navigation();
           }
       }
   };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMain();
    }


    @Override
    public int initLayout() {
        return R.layout.activity_index;
    }

    @Override
    public void reloadData() {

    }

    //启动主页
    private void startMain() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    han.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
