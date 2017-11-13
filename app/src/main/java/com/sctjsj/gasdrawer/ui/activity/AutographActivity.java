package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.ui.CustomView.LinePathView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
//签名的页面
@Route(path = "/main/act/AutographActivity")
public class AutographActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.autograph_bt_clear)
    Button autographBtClear;
    @BindView(R.id.autograph_linepath)
    LinePathView autographLinepath;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autograph);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.autograph_bt_clear, R.id.tv_affirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.autograph_bt_clear:
                //清除签名
                autographLinepath.clear();
                break;
            case R.id.tv_affirm:
                //保存并跳转
                //判断是否有签名
                if (autographLinepath.getTouched()) {
                    try {
                        autographLinepath.save(InstalActivity.path, true, 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setResult(102);
                    finish();
                } else {
                    Toast.makeText(AutographActivity.this, "您没有签名~", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
