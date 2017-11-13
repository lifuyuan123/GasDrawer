package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
 * 现场查看故障原因
 */
@Route(path = "/main/act/RepairCheck")
public class RepairCheck extends BaseAppcompatActivity {

    @BindView(R.id.repair_repairCheck_back)
    ImageView repairRepairCheckBack;
    @BindView(R.id.repair_repairCheck_btn)
    TextView repairRepairCheckBtn;
    @BindView(R.id.repair_repairCheck_edt)
    EditText repairRepairCheckEdt;
    @BindView(R.id.repair_check_rgyn)
    RadioGroup repairCheckRgyn;
    @BindView(R.id.rapair_check_rgxz)
    RadioGroup rapairCheckRgxz;
    @BindView(R.id.rad_ture)
    RadioButton radTure;
    @BindView(R.id.rad_false)
    RadioButton radFalse;
    @BindView(R.id.rad_1)
    RadioButton rad1;
    @BindView(R.id.rad_2)
    RadioButton rad2;
    @BindView(R.id.rad_3)
    RadioButton rad3;
    @BindView(R.id.rad_4)
    RadioButton rad4;
    @BindView(R.id.activity_repair_check)
    LinearLayout activityRepairCheck;

    private Map<String, String> datas;
    private String isConform = "";
    private String repairReson = "";
    private String gasType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSavaState();
        datas = new HashMap<>();
        setListener();


    }

    //获取保存的状态
    private void getSavaState() {
        if (null != CacheUtils.getCache("RepairCheck")) {
            datas = (Map<String, String>) CacheUtils.getCache("RepairCheck");
            repairRepairCheckEdt.setText(datas.get("repairReson"));
            isConform=datas.get("isConform");
            if(!TextUtils.isEmpty(isConform)) {
                switch (datas.get("isConform")) {
                    case "1":
                        radTure.setChecked(true);
                        break;
                    case "2":
                        radFalse.setChecked(true);
                        break;
                }
            }
            gasType=datas.get("gasType");
            if(!TextUtils.isEmpty(gasType)){
            switch (datas.get("gasType")) {
                case "1":
                   rad1.setChecked(true);
                    break;
                case "2":
                    rad2.setChecked(true);
                    break;
                case "3":
                    rad3.setChecked(true);
                    break;
                case "4":
                    rad4.setChecked(true);
                    break;
            }
            }
        }

    }

    //设置监听
    private void setListener() {
        repairCheckRgyn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        if (button.getText().toString().equals("是")) {
                            isConform = "1";
                        } else {
                            isConform = "2";
                        }
                    }

                }
            }
        });

        rapairCheckRgxz.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        switch (button.getText().toString()) {
                            case "民用":
                                gasType = "1";
                                break;
                            case "商业":
                                gasType = "2";
                                break;
                            case "工业":
                                gasType = "3";
                                break;
                            case "集体":
                                gasType = "4";
                                break;
                        }
                    }

                }
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_repair_check;
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
                commint();
                break;
        }
    }

    private void commint() {
        if (TextUtils.isEmpty(isConform) || TextUtils.isEmpty(gasType) || TextUtils.isEmpty(repairRepairCheckEdt.getText().toString())) {
            Toast.makeText(this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
        } else {
            datas.put("isConform", isConform);
            datas.put("repairReson", repairRepairCheckEdt.getText().toString());
            datas.put("gasType", gasType);
            Intent intent = new Intent();
            intent.putExtra("key", (Serializable) datas);
            this.setResult(107, intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveState();
    }

    //保存状态
    private void saveState() {
        CacheUtils.putCache("RepairCheck", datas);
    }

}
