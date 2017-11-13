package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.util.CacheUtils;

import java.io.Serializable;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

//选择管道
@Route(path ="/main/act/ChooseConduitActivity")
public class ChooseConduitActivity extends BaseAppcompatActivity {

    @BindView(R.id.radiobt_out)
    RadioButton radiobtOut;
    @BindView(R.id.radiobt_hide)
    RadioButton radiobtHide;
    @BindView(R.id.group_conduit)
    RadioGroup groupConduit;
    private int chooseConduit=-1;
    private HashMap<String,Integer> map=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSaveState();
        groupConduit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0; i <group.getChildCount() ; i++) {
                    RadioButton button= (RadioButton) group.getChildAt(i);
                    if(button.isChecked()){
                        String s= button.getText().toString();
                        if(s.equals("明封")){
                            chooseConduit=1;
                        }else if(s.equals("暗埋")){
                            chooseConduit=2;
                        }
                        map.put("chooseConduit",chooseConduit);
                    }
                }
            }
        });
    }

    //获取存储的状态
    private void getSaveState() {
        if(CacheUtils.getCache("ChooseConduitActivity")!=null){
        map= (HashMap<String, Integer>) CacheUtils.getCache("ChooseConduitActivity");
        chooseConduit=map.get("chooseConduit");
        switch (chooseConduit){
            case 1:
                radiobtOut.setChecked(true);
                break;
            case 2:
                radiobtHide.setChecked(true);
                break;
        }
    }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_choose_conduit;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.iv_back, R.id.erectWork_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //提交
            case R.id.erectWork_commit:
                commit();
                break;
        }
    }

    //提交
    private void commit() {
        saveState();
        Intent intent=new Intent();
        intent.putExtra("key",chooseConduit );
        setResult(106,intent);
        finish();
    }

    //保存状态
    private void saveState() {
        CacheUtils.putCache("ChooseConduitActivity",map);
    }
}
