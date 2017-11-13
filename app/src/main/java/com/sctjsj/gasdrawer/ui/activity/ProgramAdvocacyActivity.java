package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;

import butterknife.BindView;
import butterknife.OnClick;

//规划说明
@Route(path = "/main/act/ProgramAdvocacy")
public class ProgramAdvocacyActivity extends BaseAppcompatActivity {

    @BindView(R.id.program_back)
    ImageView programBack;
    @BindView(R.id.erectwork_tv_installation_mode)
    TextView erectworkTvInstallationMode;
    @BindView(R.id.program_GHdate)
    TextView programGHdate;
    @BindView(R.id.program_JFdate)
    TextView programJFdate;
    @Autowired(name = "data")
    WorkMessageBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        switch (bean.getOrdertype()){
            case 1:
                erectworkTvInstallationMode.setText("分户安装");
                break;
            case 2:
                erectworkTvInstallationMode.setText("户内改造");
                break;
            case 3:
                erectworkTvInstallationMode.setText("燃气具维修");
                break;
            case 4:
                erectworkTvInstallationMode.setText("安检整改");
                break;

        }
        programGHdate.setText(bean.getInstallTime());
        programJFdate.setText(bean.getInsertTime());
    }

    @Override
    public int initLayout() {
        return R.layout.activity_program_advocacy;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.program_back)
    public void onViewClicked() {
        finish();
    }
}
