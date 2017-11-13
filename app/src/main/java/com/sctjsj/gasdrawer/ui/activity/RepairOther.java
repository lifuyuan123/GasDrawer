package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.util.CacheUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 其他维护事项
 */
@Route(path = "/main/act/RepairOther")
public class RepairOther extends BaseAppcompatActivity {

    @BindView(R.id.repair_other_back)
    ImageView repairOtherBack;
    @BindView(R.id.repair_other_btn)
    TextView repairOtherBtn;
    @BindView(R.id.repair_other_edt)
    EditText repairOtherEdt;
    @BindView(R.id.other_sy_rg)
    RadioGroup otherSyRg;
    @BindView(R.id.repair_other_hg)
    RadioButton repairOtherHg;
    @BindView(R.id.repair_other_bhg)
    RadioButton repairOtherBhg;

    private Map<String, String> datas;
    private String other = "";
    private String qbsy = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datas = new HashMap<>();
        getState();
        repairOtherEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                other = s.toString();
            }
        });
        otherSyRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        qbsy = "1";
                    } else {
                        qbsy = "2";
                    }
                }
            }
        });


    }

    //获取状态
    private void getState() {
        if (null != CacheUtils.getCache("RepairOther")) {
            HashMap<String, String> data = (HashMap<String, String>) CacheUtils.getCache("RepairOther");
            repairOtherEdt.setText(data.get("other"));
            other=data.get("other");
            String s = data.get("qbsy");
            if (s.equals("1")) {
                qbsy="1";
                repairOtherBhg.setChecked(true);
            }else if(s.equals("2")){
                qbsy="2";
                repairOtherHg.setChecked(true);
            }
        }
    }


    @Override
    public int initLayout() {
        return R.layout.activity_repair_other;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.repair_other_back, R.id.repair_other_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.repair_other_back:
                finish();
                break;
            case R.id.repair_other_btn:
                commit();
                break;
        }
    }

    //提交
    private void commit() {
        datas.put("other", other);
        datas.put("qbsy", qbsy);
        Intent intent = new Intent();
        intent.putExtra("key", (Serializable) datas);
        this.setResult(121, intent);
        saveState();
        finish();


    }

    //保存状态
    private void saveState() {
        CacheUtils.putCache("RepairOther", datas);
    }

}
