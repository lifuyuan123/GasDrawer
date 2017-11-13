package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
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

//安装位置
@Route(path = "/main/act/Installationlocation")
public class InstallationlocationActivity extends BaseAppcompatActivity {

    @BindView(R.id.install_back)
    ImageView installBack;
    @BindView(R.id.install_affirm)
    TextView installAffirm;
    @BindView(R.id.erectwork_ok_radb_kitchen)
    RadioButton erectworkOkRadbKitchen;
    @BindView(R.id.erectwork_ok_radb_balcony)
    RadioButton erectworkOkRadbBalcony;
    @BindView(R.id.erectwork_ok_radb_other)
    RadioButton erectworkOkRadbOther;
    @BindView(R.id.group_kitchen)
    RadioGroup groupKitchen;
    @BindView(R.id.erectwork_gas_kitchen)
    RadioButton erectworkGasKitchen;
    @BindView(R.id.erectwork_gas_balcony)
    RadioButton erectworkGasBalcony;
    @BindView(R.id.erectwork_gas_other)
    RadioButton erectworkGasOther;
    @BindView(R.id.erectwork_gas_noinstall)
    RadioButton erectworkGasNoinstall;
    @BindView(R.id.group_gas_kitchen)
    RadioGroup groupGasKitchen;
    @BindView(R.id.erectwork_hot_kitchen)
    RadioButton erectworkHotKitchen;
    @BindView(R.id.erectwork_hot_balcony)
    RadioButton erectworkHotBalcony;
    @BindView(R.id.erectwork_hot_other)
    RadioButton erectworkHotOther;
    @BindView(R.id.erectwork_hot_noinstall)
    RadioButton erectworkHotNoinstall;
    @BindView(R.id.group_hot_kitchen)
    RadioGroup groupHotKitchen;
    @BindView(R.id.erectwork_twostove_kitchen)
    RadioButton erectworkTwostoveKitchen;
    @BindView(R.id.erectwork_twostove_balcony)
    RadioButton erectworkTwostoveBalcony;
    @BindView(R.id.erectwork_twostove_other)
    RadioButton erectworkTwostoveOther;
    @BindView(R.id.erectwork_twostove_noinstall)
    RadioButton erectworkTwostoveNoinstall;
    @BindView(R.id.group_twostove_kitchen)
    RadioGroup groupTwostoveKitchen;
    @BindView(R.id.erectwork_drter_chek_kitchen)
    RadioButton erectworkDrterChekKitchen;
    @BindView(R.id.erectwork_drter_chek_balcony)
    RadioButton erectworkDrterChekBalcony;
    @BindView(R.id.erectwork_drter_chek_other)
    RadioButton erectworkDrterChekOther;
    @BindView(R.id.erectwork_drter_chek_noinstall)
    RadioButton erectworkDrterChekNoinstall;
    @BindView(R.id.group_drter_chek_kitchen)
    RadioGroup groupDrterChekKitchen;

    private Map<String,String> data=new HashMap<>();
    private String installPlace="";
    private String gasPlace="";
    private String waterPlace="";
    private String gasTwoPlace="";
    private String clothePlace="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initdata();
        setListener();
        getSaveState();
    }
    //获取保存状态
    private void getSaveState() {
        if(CacheUtils.getCache("InstallationlocationActivity")!=null) {
            data= (Map<String, String>) CacheUtils.getCache("InstallationlocationActivity");
            installPlace = data.get("installPlace");
            if (installPlace != null) {
                switch (installPlace) {
                    case "厨房":
                        erectworkOkRadbKitchen.setChecked(true);
                        break;
                    case "阳台":
                        erectworkOkRadbBalcony.setChecked(true);
                        break;
                    case "其他":
                        erectworkOkRadbOther.setChecked(true);
                        break;
                }
            }
            gasPlace = data.get("gasPlace");
            if (gasPlace != null)
                switch (gasPlace) {
                    case "厨房":
                        erectworkGasKitchen.setChecked(true);
                        break;
                    case "阳台":
                        erectworkGasBalcony.setChecked(true);
                        break;
                    case "其他":
                        erectworkGasOther.setChecked(true);
                        break;
                    case "未安装":
                        erectworkGasOther.setChecked(true);
                        break;
                }
            waterPlace = data.get("waterPlace");
            if (waterPlace != null)
                switch (waterPlace) {
                    case "厨房":
                        erectworkHotKitchen.setChecked(true);
                        break;
                    case "阳台":
                        erectworkHotBalcony.setChecked(true);
                        break;
                    case "其他":
                        erectworkHotOther.setChecked(true);
                        break;
                    case "未安装":
                        erectworkHotOther.setChecked(true);
                        break;
                }
            gasTwoPlace = data.get("gasTwoPlace");
            if (gasTwoPlace != null)
                switch (gasTwoPlace) {
                    case "厨房":
                        erectworkTwostoveKitchen.setChecked(true);
                        break;
                    case "阳台":
                        erectworkTwostoveBalcony.setChecked(true);
                        break;
                    case "其他":
                        erectworkTwostoveOther.setChecked(true);
                        break;
                    case "未安装":
                        erectworkTwostoveOther.setChecked(true);
                        break;
                }
            clothePlace = data.get("clothePlace");
            if (clothePlace != null)
                switch (clothePlace) {
                    case "厨房":
                        erectworkDrterChekKitchen.setChecked(true);
                        break;
                    case "阳台":
                        erectworkDrterChekBalcony.setChecked(true);
                        break;
                    case "其他":
                        erectworkDrterChekOther.setChecked(true);
                        break;
                    case "未安装":
                        erectworkDrterChekOther.setChecked(true);
                        break;
                }
        }
    }

    private void initdata() {
        data.put("installPlace",installPlace);
        data.put("gasPlace",gasPlace);
        data.put("waterPlace",waterPlace);
        data.put("gasTwoPlace",gasTwoPlace);
        data.put("clothePlace",clothePlace);
    }

    private void setListener() {
        groupKitchen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i <group.getChildCount() ; i++) {
                    RadioButton button= (RadioButton) group.getChildAt(i);
                   if(button.isChecked()){
                       installPlace= button.getText().toString();
                       data.put("installPlace",installPlace);
                   }
                }
            }
        });

        groupGasKitchen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i <group.getChildCount() ; i++) {
                    RadioButton button= (RadioButton) group.getChildAt(i);
                    if(button.isChecked()){
                        gasPlace= button.getText().toString();
                        data.put("gasPlace",gasPlace);
                    }
                }
            }
        });

        groupHotKitchen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i <group.getChildCount() ; i++) {
                    RadioButton button= (RadioButton) group.getChildAt(i);
                    if(button.isChecked()){
                        waterPlace= button.getText().toString();
                        data.put("waterPlace",waterPlace);
                    }
                }
            }
        });

        groupTwostoveKitchen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i <group.getChildCount() ; i++) {
                    RadioButton button= (RadioButton) group.getChildAt(i);
                    if(button.isChecked()){
                        gasTwoPlace= button.getText().toString();
                        data.put("gasTwoPlace",gasTwoPlace);
                    }
                }
            }
        });

        groupDrterChekKitchen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i <group.getChildCount() ; i++) {
                    RadioButton button= (RadioButton) group.getChildAt(i);
                    if(button.isChecked()){
                        clothePlace= button.getText().toString();
                        data.put("clothePlace",clothePlace);
                    }
                }
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_installationlocation;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.install_back, R.id.install_affirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_back:
                finish();
                break;
            case R.id.install_affirm:
                Intent intent=new Intent();
                intent.putExtra("key", (Serializable) data);
                this.setResult(205,intent);
                saveState();
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    //保存状态
    private void saveState() {
        CacheUtils.putCache("InstallationlocationActivity",data);
    }

}
