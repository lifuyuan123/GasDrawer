package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 报修客户基本信息（子页）
 */
@Route(path = "/main/act/CustomerMsg")
public class CustomerMsg extends BaseAppcompatActivity {


    @BindView(R.id.repair_customer_back)
    ImageView repairCustomerBack;
    @Autowired(name = "data")
    WorkMessageBean bean;
    @BindView(R.id.customer_name)
    TextView customerName;
    @BindView(R.id.customer_Number)
    TextView customerNumber;
    @BindView(R.id.customer_orderNo)
    TextView customerOrderNo;
    @BindView(R.id.customer_ordertype)
    TextView customerOrdertype;
    @BindView(R.id.customer_date)
    TextView customerDate;
    @BindView(R.id.customer_phone)
    TextView customerPhone;
    @BindView(R.id.customer_location)
    TextView customerLocation;
    @BindView(R.id.activity_customer_msg)
    LinearLayout activityCustomerMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        if(null!=bean){
            customerName.setText(bean.getClientName());
            customerNumber.setText(bean.getAcceptedNumber());
            customerOrderNo.setText(bean.getOrderNo());
            switch (bean.getOrdertype()){
                case 1:
                    customerOrdertype.setText("分户安装 ");
                    break;
                case 2:
                    customerOrdertype.setText("户内改造");
                    break;
                case 3:
                    customerOrdertype.setText("燃气具维修");
                    break;
                case 4:
                    customerOrdertype.setText("安检整改");
                    break;
                case 5:
                    customerOrdertype.setText("其他");
                    break;

            }
            customerDate.setText(bean.getInsertTime());
            customerPhone.setText(bean.getTel());
            customerLocation.setText(bean.getAddress());
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_customer_msg;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.repair_customer_back)
    public void onViewClicked() {
        finish();
    }
}
