package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.model.LoginModule;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/login")
public class LoginActivity extends BaseAppcompatActivity implements LoginModule.LogincallBack {

    @BindView(R.id.login_userName)
    EditText loginUserName;
    @BindView(R.id.login_userPassword)
    EditText loginUserPassword;
    @BindView(R.id.login_bt)
    Button loginBt;
    private boolean Flag=false;
    private LoginModule module=new LoginModule(LoginActivity.this,this);
    private String userName=null;
    private String userPas=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListener();
    }


    private void setListener() {
        loginUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //做用户名的效验
                if(TextUtils.isEmpty(s)){
                    loginUserName.setError("输入的用户名不合法！");
                }else {
                    userName=s.toString();
                }
            }
        });

        loginUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //密码的效验
                if(TextUtils.isEmpty(s)){
                    loginUserPassword.setError("输入的密码名不合法！");
                }else {
                    userPas=s.toString();
                }
            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.login_bt)
    public void onViewClicked() {
       if(null!=userPas&&null!=userName){
           module.login(userName,userPas);
       }
    }

    @Override
    public void succeed() {
        Toast.makeText(this,"登陆成功",Toast.LENGTH_LONG).show();
        ARouter.getInstance().build("/user/act/index").navigation();
        finish();
    }
}
