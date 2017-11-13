package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.util.CacheUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

//支付方式
@Route(path = "/main/act/PayStyleActivity")
public class PayStyleActivity extends BaseAppcompatActivity {

    @BindView(R.id.other_sy_rg)
    RadioGroup otherSyRg;
    @BindView(R.id.other_sy_ispay)
    RadioGroup otherSyIspay;
    @BindView(R.id.pay_style_sm)
    RadioButton payStyleSm;
    @BindView(R.id.pay_style_gzry)
    RadioButton payStyleGzry;
    @BindView(R.id.pay_style_yyt)
    RadioButton payStyleYyt;
    @BindView(R.id.pay_style_ysf)
    RadioButton payStyleYsf;
    @BindView(R.id.pay_style_wsf)
    RadioButton payStyleWsf;
    private int payment = -1;
    private int ispay = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSavaState();
        otherSyRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        switch (button.getText().toString()) {
                            case "扫码支付":
                                payment = 1;
                                break;
                            case "工作人员代收":
                                payment = 2;
                                break;
                            case "营业厅缴费":
                                payment = 3;
                                break;
                        }
                    }

                }
            }
        });

        otherSyIspay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        switch (button.getText().toString()) {
                            case "已收费":
                                ispay = 1;
                                break;
                            case "未收费":
                                ispay = 2;
                                break;
                        }
                    }
                }
            }
        });
    }


    //获取存储状态
    private void getSavaState() {
        if (null != CacheUtils.getCache("PayStyleActivity")) {
            Map<String, Integer> data = (Map<String, Integer>) CacheUtils.getCache("PayStyleActivity");
             payment=data.get("key");
           switch (payment){
               case 1:
                   payStyleSm.setChecked(true);
                   break;
               case 2:
                   payStyleGzry.setChecked(true);
                   break;
               case 3:
                   payStyleYyt.setChecked(true);
                   break;
           }

            ispay=data.get("keys");
            if(ispay==1){
                payStyleYsf.setChecked(true);
            }else if(ispay==2){
                payStyleWsf.setChecked(true);
            }

        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_pay_style;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.repair_repairCheck_back, R.id.repair_repairCheck_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.repair_repairCheck_back:
                finish();
                break;
            case R.id.repair_repairCheck_btn:
                //提交
                commit();
                break;
        }
    }

    private void commit() {
        Log.e("paytype", "pament=" + payment + "   ispay=" + ispay);
        if (payment != -1 && ispay != -1) {

            Intent intent = new Intent();
            intent.putExtra("key", payment);
            intent.putExtra("keys", ispay);
            this.setResult(108, intent);
            finish();
        } else {
            if (payment == -1) {
                Toast.makeText(this, "请选择收费方式。", Toast.LENGTH_SHORT).show();
            }
            if (ispay == -1) {
                Toast.makeText(this, "请选择是否已收款。", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveState();
    }

    //保存状态
    private void saveState() {
        Map<String, Integer> data = new HashMap<>();
        data.put("key", payment);
        data.put("keys", ispay);
        CacheUtils.putCache("PayStyleActivity", data);
    }
}
