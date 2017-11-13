package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.model.ChangPasModule;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 修改密码界面
 */
@Route(path = "/main/act/ChangPswActivity")
public class ChangPswActivity extends BaseAppcompatActivity implements ChangPasModule.ChangPasModuleCallBack {

    @BindView(R.id.mine_changPsw_back)
    ImageView mineChangPswBack;
    @BindView(R.id.mine_changPsw_oldpsw)
    EditText mineChangPswOldpsw;
    @BindView(R.id.mine_changPsw_newpsw)
    EditText mineChangPswNewpsw;
    @BindView(R.id.mine_changPsw_sure)
    Button mineChangPswSure;
    @BindView(R.id.mine_changPsw_newpswAgin)
    EditText mineChangPswNewpswAgin;

    private String oldPas;
    private String newPas;
    private String newPasAgin;
    private ChangPasModule module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        module = new ChangPasModule(this);
        initView();

    }

    private void initView() {
        mineChangPswOldpsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    oldPas = s.toString();
                } else {
                    mineChangPswOldpsw.setError("输入的密码不合法！");
                }


            }
        });

        mineChangPswNewpsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    newPas = s.toString();
                } else {
                    mineChangPswNewpsw.setError("输入的密码不合法！");
                }
            }
        });

        mineChangPswNewpswAgin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    newPasAgin = s.toString();
                } else {
                    mineChangPswNewpswAgin.setError("输入的密码不合法！");
                }
            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_chang_psw;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.mine_changPsw_back, R.id.mine_changPsw_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_changPsw_back:
                finish();
                break;
            case R.id.mine_changPsw_sure:
                if (!TextUtils.isEmpty(oldPas) && !TextUtils.isEmpty(newPas)&&!TextUtils.isEmpty(newPasAgin)) {
                    if (newPas.equals(newPasAgin)) {
                        if (!oldPas.equals(newPas)) {
                            //访问网络
                            module.changPas(oldPas, newPas);
                        } else {
                            Toast.makeText(this, "请重新输入新密码！", Toast.LENGTH_SHORT).show();
                            mineChangPswNewpsw.setError("输入的密码不合法！");
                        }
                    } else {
                        Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "密码不能为空。", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void changSucceed() {
        Toast.makeText(this, "修改密码成功！", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post("out");
        finish();
    }

    @Override
    public void changDefeat() {
        Toast.makeText(this, "密码不匹配，修改密码失败！", Toast.LENGTH_SHORT).show();
    }
}
