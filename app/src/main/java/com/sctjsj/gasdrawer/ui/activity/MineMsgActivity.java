package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.WorkerBean;
import com.sctjsj.gasdrawer.model.MineModule;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/MineMsgActivity")
public class MineMsgActivity extends BaseAppcompatActivity implements MineModule.MineModuleCallBack{

    @BindView(R.id.mine_msg_back)
    ImageView mineMsgBack;
    @BindView(R.id.mine_msg_name)
    TextView mineMsgName;
    @BindView(R.id.mine_msg_work)
    TextView mineMsgWork;
    @BindView(R.id.mine_msg_phone)
    TextView mineMsgPhone;
    @Autowired(name = "worker")
    WorkerBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        new MineModule(this, this).getWorkerMsg();
    }

    private void initView() {
       if(null!=bean){
           mineMsgName.setText(bean.getUsername());
           if(bean.getType()==1){
               mineMsgWork.setText("安装工程师");
           }else if(bean.getType()==2){
               mineMsgWork.setText("维修工程师");
           }else if(bean.getType()==3){
               mineMsgWork.setText("审核工程师");
           }
           mineMsgPhone.setText(bean.getPhone());
       }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_mine_msg;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.mine_msg_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void getMineMsg(WorkerBean bean) {
        Log.e("bean",bean.toString());
    }

    @Override
    public void changIconSucceed() {

    }
}
