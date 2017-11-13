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

/***
 * 维修派工信息（子页）
 */
@Route(path = "/main/act/RepairWork")
public class RepairWork extends BaseAppcompatActivity {
    @Autowired(name = "data")
    WorkMessageBean bean;

    @BindView(R.id.repair_repairWork_back)
    ImageView repairRepairWorkBack;
    @BindView(R.id.repair_name)
    TextView repairName;
    @BindView(R.id.repair_mark)
    TextView repairMark;
    @BindView(R.id.repair_date)
    TextView repairDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        if(null!=bean){
            repairName.setText((String)SPFUtil.get(Tag.TAG_NAME,"不详"));
            repairMark.setText(bean.getRepairReason());
            repairDate.setText(bean.getInstallTime());
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_repair_work;
    }

    @Override
    public void reloadData() {

    }


    @OnClick(R.id.repair_repairWork_back)
    public void onViewClicked() {
        finish();
    }
}
