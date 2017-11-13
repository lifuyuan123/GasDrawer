package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.ListViewUtil;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.GroupBean;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.presenter.adapter.ServerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/AddServerActivity")
//添加服务
public class AddServerActivity extends BaseAppcompatActivity {
    @BindView(R.id.tv_server_change)
    TextView tvServerChange;
    @BindView(R.id.Instalact_Img_SerAdd)
    ImageView InstalactImgSerAdd;
    @BindView(R.id.Instalact_LtV_Ser)
    ListView InstalactLtVSer;
    private List<GroupBean> list = new ArrayList<>();
    private List<List<ServerChildBean>> serverChild = new ArrayList<>();
    private List<ServerChildBean> list1;
    private Intent intent;
    private Bundle bundle;
    private int distype;
    private int length;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(this, ChooseCostActivity.class);
        bundle = new Bundle();
        getdata();
    }

    private void getdata() {
        Intent intent = getIntent();
        Bundle bun = intent.getBundleExtra("key");
        list = (List<GroupBean>) bun.getSerializable("list");
        serverChild = (List<List<ServerChildBean>>) bun.getSerializable("data");
    }

    @Override
    public int initLayout() {
        return R.layout.activity_add_server;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.Instalact_Img_back, R.id.tv_server_change, R.id.Instalact_Img_SerAdd,R.id.instal_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Instalact_Img_back:
                finish();
                break;
            case R.id.tv_server_change:
                bundle.putSerializable("list", (Serializable) list);
                bundle.putSerializable("data", (Serializable) serverChild);
                intent.putExtra("key", bundle);
                startActivityForResult(intent, 100);
                break;
            case R.id.Instalact_Img_SerAdd:
                bundle.putSerializable("list", (Serializable) list);
                bundle.putSerializable("data", (Serializable) serverChild);
                intent.putExtra("key", bundle);
                startActivityForResult(intent, 100);
                break;
            case R.id.instal_up:
                Intent intent1=new Intent();
                Bundle bundle1=new Bundle();
                bundle1.putSerializable("data", (Serializable) list1);
                intent1.putExtra("data",bundle1);
                intent1.putExtra("distype",distype);
                intent1.putExtra("materialall",length);
                setResult(251,intent1);
                finish();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 101) {
            Bundle data2 = data.getBundleExtra("data");
            list1 = (List<ServerChildBean>) data2.getSerializable("data");
            distype=data.getIntExtra("distype",2);
            length=data.getIntExtra("materialall",0);
            ServerAdapter adapter1 = new ServerAdapter(list1, this);
            InstalactLtVSer.setAdapter(adapter1);
            ListViewUtil.setListViewHeightBasedOnChildren(InstalactLtVSer);
            InstalactLtVSer.setVisibility(View.VISIBLE);
            InstalactImgSerAdd.setVisibility(View.GONE);
            tvServerChange.setVisibility(View.VISIBLE);
        }
    }

}
