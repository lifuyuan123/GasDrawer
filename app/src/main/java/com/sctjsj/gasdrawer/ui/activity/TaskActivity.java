package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;
import com.sctjsj.gasdrawer.ui.CustomView.CustomGridView;

import butterknife.BindView;
import butterknife.OnClick;

//任务详情
@Route(path = "/main/act/TaskActivity")
public class TaskActivity extends BaseAppcompatActivity {
    @Autowired(name = "data")
    WorkMessageBean bean;
    @BindView(R.id.Instalact_Img_back)
    ImageView InstalactImgBack;
    @BindView(R.id.audit_Txt_YWtype)
    TextView auditTxtYWtype;
    @BindView(R.id.audit_Txt_WorkNumber)
    TextView auditTxtWorkNumber;
    @BindView(R.id.audit_Txt_CAE)
    TextView auditTxtCAE;
    @BindView(R.id.audit_Txt_ordreNumber)
    TextView auditTxtOrdreNumber;
    @BindView(R.id.Instalact_Txt_phone)
    TextView InstalactTxtPhone;
    @BindView(R.id.audit_Txt_location)
    TextView auditTxtLocation;
    @BindView(R.id.audit_Txt_planner)
    TextView auditTxtPlanner;
    @BindView(R.id.audit_Txt_date)
    TextView auditTxtDate;
    @BindView(R.id.audit_Txt_worker)
    TextView auditTxtWorker;
    @BindView(R.id.audit_Txt_deparName)
    TextView auditTxtDeparName;
    @BindView(R.id.audit_Txt_dates)
    TextView auditTxtDates;
//    @BindView(R.id.audit_LtV_CL)
//    ListView auditLtVCL;
//    @BindView(R.id.audit_LtV_Ser)
//    ListView auditLtVSer;
//    @BindView(R.id.audit_GreadView)
//    CustomGridView auditGreadView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        auditTxtYWtype.setText(bean.getClientName());
        auditTxtWorkNumber.setText(bean.getHouseName());
        auditTxtCAE.setText(bean.getCAENNumber());
        auditTxtOrdreNumber.setText(bean.getOrderNo());
        InstalactTxtPhone.setText(bean.getTel());
        auditTxtLocation.setText(bean.getAddress());
        auditTxtPlanner.setText(bean.getPlanner());
        auditTxtDate.setText(bean.getInsertTime());
        auditTxtWorker.setText((String) SPFUtil.get(Tag.TAG_NAME, "不详"));
        switch (bean.getOrdertype()) {
            case 1:
                auditTxtDeparName.setText("分户安装 ");
                break;
            case 2:
                auditTxtDeparName.setText("户内改造");
                break;
            case 3:
                auditTxtDeparName.setText("燃气具维修");
                break;
            case 4:
                auditTxtDeparName.setText("安检整改");
                break;
            case 5:
                auditTxtDeparName.setText("其他");
                break;

        }
        auditTxtDates.setText(bean.getInstallTime());
    }

    @Override
    public int initLayout() {
        return R.layout.activity_task;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.Instalact_Img_back)
    public void onViewClicked() {
        finish();
    }
}
