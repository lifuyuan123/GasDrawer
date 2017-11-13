package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.util.CacheUtils;

import butterknife.BindView;
import butterknife.OnClick;

//选择优惠
@Route(path = "/main/act/ChooseDiscountActivity")
public class ChooseDiscountActivity extends BaseAppcompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.other_sy_ispay)
    RadioGroup otherSyIspay;
    @BindView(R.id.radiobutton_join)
    RadioButton radiobuttonJoin;
    @BindView(R.id.radiobutton_nojoin)
    RadioButton radiobuttonNojoin;
    private int distype = 2;
    private int type = -1;
    private String hisDistype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getIntExtra("data", 2);
        if (type == 1) {
            tvTitle.setText("材料优惠");
        } else {
            tvTitle.setText("服务优惠");
        }
        getHistory();

        otherSyIspay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        switch (button.getText().toString()) {
                            case "参加":
                                distype = 1;
                                break;
                            case "不参加":
                                distype = 2;
                                break;
                        }
                    }

                }
            }
        });
    }

    //获取历史数据
    private void getHistory() {
        if(type==1){
            hisDistype = (String) CacheUtils.getCache("ChooseDiscountActivity1");
        }else if(type==2){
            hisDistype = (String) CacheUtils.getCache("ChooseDiscountActivity2");
        }

        if (!TextUtils.isEmpty(hisDistype)) {
         switch (hisDistype){
             case "1":
                 radiobuttonJoin.setChecked(true);
                 break;
             case "2":
                 radiobuttonNojoin.setChecked(true);
                 break;
         }
        }

    }

    @Override
    public int initLayout() {
        return R.layout.activity_choose_discount;
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
            //确定
            case R.id.repair_repairCheck_btn:
                comit();
                break;
        }
    }

    //提交
    private void comit() {
        Intent intent = new Intent();
        intent.putExtra("key", distype);
        if (type == 1) {
            setResult(201, intent);
            saveData1(distype);
        } else if (type == 2) {
            setResult(202, intent);
            saveData2(distype);
        }

        finish();
    }

    //保存状态
    private void saveData1(int distype) {
        CacheUtils.putCache("ChooseDiscountActivity1", String.valueOf(distype));
    }
    //保存状态
    private void saveData2(int distype) {
        CacheUtils.putCache("ChooseDiscountActivity2", String.valueOf(distype));
    }

}
