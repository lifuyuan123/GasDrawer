package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;

import butterknife.BindView;
import butterknife.OnClick;

//派工记录
@Route(path = "/main/act/DispatchRecord")
public class DispatchRecordActivity extends BaseAppcompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.erectwork_tv_planuser)
    TextView erectworkTvPlanuser;
    @BindView(R.id.erectwork_tv_installuser)
    TextView erectworkTvInstalluser;
    @BindView(R.id.erectwork_tv_installdata)
    TextView erectworkTvInstalldata;
    @Autowired(name = "data")
    WorkMessageBean bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        erectworkTvPlanuser.setText(bean.getPlanner());
        erectworkTvInstalluser.setText( (String)SPFUtil.get(Tag.TAG_NAME,"不详"));
        erectworkTvInstalldata.setText(bean.getInsertTime());
    }

    @Override
    public int initLayout() {
        return R.layout.activity_dispatch_record;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
