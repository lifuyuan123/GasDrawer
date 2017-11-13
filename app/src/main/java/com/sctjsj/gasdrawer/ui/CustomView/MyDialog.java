package com.sctjsj.gasdrawer.ui.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.sctjsj.gasdrawer.R;

/**
 * Created by Administrator on 2017/5/3.
 */

public class MyDialog extends Dialog {
    private TextView title,message;
    private String Strtitle,Strmessage;

    public MyDialog(Context context) {
        super(context,R.style.alert_dialog);
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        initview();
        initdata();
    }

    //设置title和message
    private void initdata() {
        if(Strtitle!=null&& !TextUtils.isEmpty(Strtitle)){
            title.setText(Strtitle);
        }
        if(Strmessage!=null&&!TextUtils.isEmpty(Strmessage)){
            message.setText(Strmessage);
        }
    }

    //初始化控件
    private void initview() {
        title= (TextView) findViewById(R.id.title);
        message= (TextView) findViewById(R.id.message);
    }

    //从外界Activity为Dialog设置标题
    public void setTitle(String title) {
        Strtitle = title;
    }

     //从外界Activity为Dialog设置dialog的message
    public void setMessage(String message) {
        Strmessage = message;
    }
}
