package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import com.google.gson.Gson;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.CacheBean;
import com.sctjsj.gasdrawer.util.CacheUtils;
import com.sctjsj.gasdrawer.util.MyDbHelper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 故障与维修情况（子页）
 */
@Route(path = "/main/act/RepairCase")
public class RepairCase extends BaseAppcompatActivity {

    @BindView(R.id.repair_case_back)
    ImageView repairCaseBack;
    @BindView(R.id.repair_case_btn)
    TextView repairCaseBtn;
    @BindView(R.id.radb_1)
    RadioButton radb1;
    @BindView(R.id.radb_2)
    RadioButton radb2;
    @BindView(R.id.radb_3)
    RadioButton radb3;
    @BindView(R.id.radb_4)
    RadioButton radb4;
    @BindView(R.id.radg_top)
    RadioGroup radgTop;
    @BindView(R.id.radb_5)
    RadioButton radb5;
    @BindView(R.id.radb_6)
    RadioButton radb6;
    @BindView(R.id.radb_7)
    RadioButton radb7;
    @BindView(R.id.radb_8)
    RadioButton radb8;
    @BindView(R.id.radg_buttom)
    RadioGroup radgButtom;

    String strBtnSelected = "";//记录选择的是哪个选项(两排radiobutton)
    @BindView(R.id.repair_case_rgyl)
    RadioGroup repairCaseRgyl;
    @BindView(R.id.repair_case_edaz)
    EditText repairCaseEdaz;
    @BindView(R.id.repair_case_edaj)
    EditText repairCaseEdaj;
    @BindView(R.id.activity_repair_case)
    LinearLayout activityRepairCase;
    @BindView(R.id.repair_case_rgxl)
    RadioGroup repairCaseRgxl;
    @BindView(R.id.repair_dy)
    RadioButton repairDy;
    @BindView(R.id.repair_zy)
    RadioButton repairZy;
    @BindView(R.id.repair_sl)
    RadioButton repairSl;
    @BindView(R.id.repair_dx)
    RadioButton repairDx;
    @BindView(R.id.repair_sw)
    RadioButton repairSw;
    private BtnSelected btnSelected1, btnSelected2, btnSelected3, btnSelected4, btnSelected5, btnSelected6, btnSelected7, btnSelected8;

    private String pressure = "";
    private String material = "";
    private String installDate = "";
    private String securityDate = "";
    private String leakage = "";
    private Map<String, String> datas;
    private MyDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datas = new HashMap<>();
        dbHelper = new MyDbHelper(this);
        if (!TextUtils.isEmpty(dbHelper.getJson())) {
            datas = new Gson().fromJson(dbHelper.getJson(), CacheBean.class).getDatas();
            pressure = datas.get("pressure");
            material = datas.get("material");
            leakage = datas.get("leakage");
            repairCaseEdaz.setText(datas.get("installDate"));
            repairCaseEdaj.setText(datas.get("securityDate"));

        }
        setListener();
        getSaveCache();
    }

    //获取缓存
    private void getSaveCache() {
        if (null != CacheUtils.getCache("RepairCase")) {
            datas = (Map<String, String>) CacheUtils.getCache("RepairCase");
            pressure=datas.get("pressure");
            if(!TextUtils.isEmpty(pressure)){
            switch (pressure){
                case "1":
                    repairDy.setChecked(true);
                    break;
                case "2":
                    repairZy.setChecked(true);
                    break;
            }
            }
            leakage=datas.get("leakage");
            material=datas.get("material");
            if(!TextUtils.isEmpty(leakage)){
            switch (leakage){
                case "室内":
                    repairSl.setChecked(true);
                    break;
                case "地下":
                    repairDx.setChecked(true);
                    break;
                case "室外":
                    repairSw.setChecked(true);
                    break;
            }
            }

            if(!TextUtils.isEmpty(material)) {
                switch (material) {
                    case "镀锌管":
                        radb1.setChecked(true);
                        break;
                    case "波纹管":
                        radb2.setChecked(true);
                        break;
                    case "薄壁管":
                        radb3.setChecked(true);
                        break;
                    case "PE管":
                        radb4.setChecked(true);
                        break;
                    case "无缝管":
                        radb5.setChecked(true);
                        break;
                    case "塑料管":
                        radb6.setChecked(true);
                        break;
                    case "软管":
                        radb7.setChecked(true);
                        break;
                    case "其他":
                        radb8.setChecked(true);
                        break;
                }
            }
        }
        installDate=datas.get("installDate");
        securityDate=datas.get("securityDate");
        if(!TextUtils.isEmpty(installDate)){
            repairCaseEdaz.setText(datas.get("installDate"));
        }
        if(!TextUtils.isEmpty(securityDate)){
            repairCaseEdaj.setText(datas.get("securityDate"));
        }

    }

    private void setListener() {
        repairCaseRgyl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        if (button.getText().toString().equals("低压")) {
                            pressure = "1";
                        } else {
                            pressure = "2";
                        }
                    }

                }
                if (!TextUtils.isEmpty(pressure)) {
                    if (pressure.equals("1")) {
                        ((RadioButton) group.getChildAt(0)).setChecked(true);
                    } else {
                        ((RadioButton) group.getChildAt(1)).setChecked(true);
                    }
                }

            }
        });

        repairCaseRgxl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        switch (button.getText().toString()) {
                            case "室内":
                                leakage = "室内";
                                break;
                            case "地下":
                                leakage = "地下";
                                break;
                            case "室外":
                                leakage = "室外";
                                break;
                        }
                    }

                }

                if (!TextUtils.isEmpty(leakage)) {
                    if (leakage.equals("室内")) {
                        ((RadioButton) group.getChildAt(0)).setChecked(true);
                    } else if (leakage.equals("地下")) {
                        ((RadioButton) group.getChildAt(1)).setChecked(true);
                    } else if (leakage.equals("室外")) {
                        ((RadioButton) group.getChildAt(2)).setChecked(true);
                    }
                }
            }
        });

//        radgTop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                for (int i = 0; i < group.getChildCount(); i++) {
//                    RadioButton button = (RadioButton) group.getChildAt(i);
//                    if (button.isChecked()) {
//                        switch (button.getText().toString()) {
//                            case "镀锌管":
//                                material = "镀锌管";
//                                break;
//                            case "波纹管":
//                                material = "波纹管";
//                                break;
//                            case "薄壁管":
//                                material = "薄壁管";
//                                break;
//                            case "PE管":
//                                material = "PE管";
//                                break;
//                        }
//                    }
//
//                    if (!TextUtils.isEmpty(material)) {
//                        if (material.equals("镀锌管")) {
//                            ((RadioButton) group.getChildAt(0)).setChecked(true);
//                        } else if (material.equals("波纹管")) {
//                            ((RadioButton) group.getChildAt(1)).setChecked(true);
//                        } else if (material.equals("薄壁管")) {
//                            ((RadioButton) group.getChildAt(2)).setChecked(true);
//                        } else if (material.equals("PE管")) {
//                            ((RadioButton) group.getChildAt(3)).setChecked(true);
//                        }
//                    }
//
//                }
//            }
//        });
//
//        radgButtom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                for (int i = 0; i < group.getChildCount(); i++) {
//                    RadioButton button = (RadioButton) group.getChildAt(i);
//                    if (button.isChecked()) {
//                        switch (button.getText().toString()) {
//                            case "无缝管":
//                                material = "无缝管";
//                                break;
//                            case "塑料管":
//                                material = "塑料管";
//                                break;
//                            case "软管":
//                                material = "软管";
//                                break;
//                            case "其他":
//                                material = "其他";
//                                break;
//                        }
//                    }
//
//                    if (!TextUtils.isEmpty(material)) {
//                        if (material.equals("无缝管")) {
//                            ((RadioButton) group.getChildAt(0)).setChecked(true);
//                        } else if (material.equals("塑料管")) {
//                            ((RadioButton) group.getChildAt(1)).setChecked(true);
//                        } else if (material.equals("软管")) {
//                            ((RadioButton) group.getChildAt(2)).setChecked(true);
//                        } else if (material.equals("其他")) {
//                            ((RadioButton) group.getChildAt(3)).setChecked(true);
//                        }
//                    }
//
//                }
//            }
//        });


        //自定义监听
        btnSelected1 = new BtnSelected("1");
        btnSelected2 = new BtnSelected("2");
        btnSelected3 = new BtnSelected("3");
        btnSelected4 = new BtnSelected("4");
        btnSelected5 = new BtnSelected("5");
        btnSelected6 = new BtnSelected("6");
        btnSelected7 = new BtnSelected("7");
        btnSelected8 = new BtnSelected("8");

        radb1.setOnClickListener(btnSelected1);
        radb2.setOnClickListener(btnSelected2);
        radb3.setOnClickListener(btnSelected3);
        radb4.setOnClickListener(btnSelected4);
        radb5.setOnClickListener(btnSelected5);
        radb6.setOnClickListener(btnSelected6);
        radb7.setOnClickListener(btnSelected7);
        radb8.setOnClickListener(btnSelected8);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_repair_case;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.repair_case_back, R.id.repair_case_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.repair_case_back:
                finish();
                break;
            case R.id.repair_case_btn:
                commit();
                break;
        }
    }

    //提交数据
    private void commit() {
        if (TextUtils.isEmpty(repairCaseEdaz.getText().toString()) || TextUtils.isEmpty(repairCaseEdaj.getText().toString())) {
            Toast.makeText(this, "请完善相关信息!", Toast.LENGTH_SHORT).show();
        } else {
            datas.put("pressure", pressure);
            datas.put("material", material);
            datas.put("leakage", leakage);
            datas.put("installDate", repairCaseEdaz.getText().toString());
            datas.put("securityDate", repairCaseEdaj.getText().toString());
            Intent mIntent = new Intent();
            mIntent.putExtra("key", (Serializable) datas);
            this.setResult(105, mIntent);
            finish();
        }
    }


    //点击事件的监听器，多行单选监听
    public class BtnSelected implements View.OnClickListener {
        private String btnId;

        public BtnSelected(String str) {
            btnId = str;
        }

        @Override
        public void onClick(View v) {
            strBtnSelected = btnId;//选择的某一项
//                    isSelect = true;
            //点击了第一行　，就把另外行的点击项清空
            if (btnId.equals("1") || btnId.equals("2") || btnId.equals("3") || btnId.equals("4")) {
                switch (btnId) {
                    case "1":
                        material = "镀锌管";
                        break;
                    case "2":
                        material = "波纹管";
                        break;
                    case "3":
                        material = "薄壁管";
                        break;
                    case "4":
                        material = "PE管";
                        break;
                }
                radgButtom.clearCheck();
            } else if (btnId.equals("5") || btnId.equals("6") || btnId.equals("7") || btnId.equals("8")) {
                switch (btnId) {
                    case "5":
                        material = "无缝管";
                        break;
                    case "6":
                        material = "塑料管";
                        break;
                    case "7":
                        material = "软管";
                        break;
                    case "8":
                        material = "其他";
                        break;
                }

                radgTop.clearCheck();
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
        CacheUtils.putCache("RepairCase", datas);
    }
}
