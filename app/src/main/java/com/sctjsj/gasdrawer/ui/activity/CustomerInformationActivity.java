package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.CustomerBean;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;
import com.sctjsj.gasdrawer.model.InstalModule;

import butterknife.BindView;
import butterknife.OnClick;

//客户基本信息
@Route(path = "/main/act/CustomerInformation")
public class CustomerInformationActivity extends BaseAppcompatActivity {

    @BindView(R.id.erectwork_tv_CEAnumber)
    TextView erectworkTvCEAnumber;
    @BindView(R.id.erectwork_tv_worknumber)
    TextView erectworkTvWorknumber;
    @BindView(R.id.erectwork_tv_usernumber)
    TextView erectworkTvUsernumber;
    @BindView(R.id.erectwork_tv_username)
    TextView erectworkTvUsername;
    @BindView(R.id.erectwork_tv_userphone)
    TextView erectworkTvUserphone;
    @BindView(R.id.erectwork_tv_userAdress)
    TextView erectworkTvUserAdress;
    @Autowired(name = "data")
    WorkMessageBean bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCusData();
    }
    //获取用户信息
    private void getCusData() {
        erectworkTvCEAnumber.setText(bean.getCAENNumber());
        erectworkTvWorknumber.setText(bean.getOrderNo());
        erectworkTvUsernumber.setText(bean.getHouseId());
        erectworkTvUsername.setText(bean.getHouseName());
        erectworkTvUserphone.setText(bean.getTel());
        erectworkTvUserAdress.setText(bean.getAddress());
    }

    @Override
    public int initLayout() {
        return R.layout.activity_customer_information;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

}
