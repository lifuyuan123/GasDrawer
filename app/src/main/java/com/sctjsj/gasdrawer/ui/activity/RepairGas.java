package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.RepairGasBean;
import com.sctjsj.gasdrawer.presenter.adapter.RepairGasAdapter;
import com.sctjsj.gasdrawer.util.CacheUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 燃具维修（子页面）
 */
@Route(path = "/main/act/RepairGasBean")
public class RepairGas extends BaseAppcompatActivity {

    @BindView(R.id.repair_gas_back)
    ImageView repairGasBack;
    @BindView(R.id.repair_gas_btn)
    TextView repairGasBtn;
    @BindView(R.id.repair_gas_list)
    ListView repairGasList;
    private RepairGasAdapter adapter;
    private List<RepairGasBean> data=new ArrayList<>();
    private View mView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        setAdapter();
        getSaveSate();
    }

    //获取保存的状态
    private void getSaveSate() {
        if(null!=CacheUtils.getCache("RepairGas")){
            data= (List<RepairGasBean>) CacheUtils.getCache("RepairGas");
            if(data!=null){
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
        mView = LayoutInflater.from(this).inflate(R.layout.repair_gas_itemfood,null);
        linearLayout= (LinearLayout) mView.findViewById(R.id.food_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.add(new RepairGasBean());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        data.add(new RepairGasBean());
    }

    private void setAdapter() {
        adapter=new RepairGasAdapter(this,data);
        repairGasList.addFooterView(mView);
        repairGasList.setAdapter(adapter);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_repair_gas;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.repair_gas_back, R.id.repair_gas_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.repair_gas_back:
                finish();
                break;
            case R.id.repair_gas_btn:
                Intent intent=new Intent();
                intent.putExtra("key", (Serializable) data);
                saveState();
                setResult(106,intent);
                finish();
                break;
        }
    }

    //保存状态
    private void saveState() {
        CacheUtils.putCache("RepairGas",data);
    }
}
