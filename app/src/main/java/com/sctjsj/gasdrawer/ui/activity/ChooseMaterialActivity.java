package com.sctjsj.gasdrawer.ui.activity;

import android.media.DrmInitData;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ServicesItems;
import com.sctjsj.gasdrawer.presenter.IChooseMaterialPresenter;
import com.sctjsj.gasdrawer.presenter.impl.ChooseMaterialPresenterImpl;
import com.sctjsj.gasdrawer.ui.view.IChooseMateriaActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择材料的页面
 */
@Route(path ="/main/act/ChooseMaterialActivity")
public class ChooseMaterialActivity extends BaseAppcompatActivity implements IChooseMateriaActivity {

    @BindView(R.id.Choose_Material_back)
    ImageView ChooseMaterialBack;
    @BindView(R.id.Choose_Material_btn)
    TextView ChooseMaterialBtn;
    @BindView(R.id.Choose_Material_Listview)
    ExpandableListView ChooseMaterialListview;
    private IChooseMaterialPresenter presenter;
    private List<String> groupData;
    private List<List<ServicesItems>> childData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    //初始化
    private void init() {
        presenter=new ChooseMaterialPresenterImpl(this,this);
        initData();
    }

    private void initData() {
        groupData=presenter.initGroupData();
        childData = presenter.initChildData();
        presenter.init();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_choose_material;
    }

    @Override
    public void reloadData() {
    }

    @OnClick({R.id.Choose_Material_back, R.id.Choose_Material_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Choose_Material_back://返回的监听
                finish();
                break;
            case R.id.Choose_Material_btn://确认提交的按钮
                break;
        }
    }
    //設置Adapter
    @Override
    public void setAdapter() {

    }
}
