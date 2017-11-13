package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.ListViewUtil;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.GroupBean;
import com.sctjsj.gasdrawer.presenter.adapter.SelectMaterialAdapter;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/MaterialActivity")
//材料清单
public class MaterialActivity extends BaseAppcompatActivity {

    @BindView(R.id.instal_up)
    TextView instalUp;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.Instalact_Img_CLAdd)
    ImageView InstalactImgCLAdd;
    @BindView(R.id.Instalact_LtV_CL)
    ListView InstalactLtVCL;
    private List<GroupBean> list;
    private List<List<ChildBean>> data;
    private List<ChildBean> childset;
    private Intent mIntent;
    private Bundle mBundle;
    private int distype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntent = new Intent(this, MaterialUselistActivity.class);
        mBundle = new Bundle();
        getdata();
    }

    private void getdata() {
        Intent intent = getIntent();
        Bundle bun = intent.getBundleExtra("key");
        list = (List<GroupBean>) bun.getSerializable("list");
        data = (List<List<ChildBean>>) bun.getSerializable("data");
    }

    @Override
    public int initLayout() {
        return R.layout.activity_material;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.Instalact_Img_back, R.id.instal_up, R.id.tv_change, R.id.Instalact_Img_CLAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Instalact_Img_back:
                finish();
                break;
            case R.id.instal_up:
                Intent intent1 = new Intent();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("data", (Serializable) childset);
                intent1.putExtra("data", bundle1);
                intent1.putExtra("distype",distype);
                setResult(252, intent1);
                finish();
                break;
            case R.id.tv_change:
                mBundle.putSerializable("list", (Serializable) list);
                mBundle.putSerializable("data", (Serializable) data);
                mIntent.putExtra("key", mBundle);
                startActivityForResult(mIntent, 100);
                break;
            case R.id.Instalact_Img_CLAdd:
                mBundle.putSerializable("list", (Serializable) list);
                mBundle.putSerializable("data", (Serializable) data);
                mIntent.putExtra("key", mBundle);
                startActivityForResult(mIntent, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 103) {

            Bundle data1 = data.getBundleExtra("data");
            childset = (List<ChildBean>) data1.getSerializable("data");
            distype=data.getIntExtra("distype",2);
            SelectMaterialAdapter adapter = new SelectMaterialAdapter(childset, this);
            InstalactLtVCL.setAdapter(adapter);
            ListViewUtil.setListViewHeightBasedOnChildren(InstalactLtVCL);
            InstalactLtVCL.setVisibility(View.VISIBLE);
            InstalactImgCLAdd.setVisibility(View.GONE);
            tvChange.setVisibility(View.VISIBLE);
            Log.e("distypeMA",distype+"");
        }
    }

}
