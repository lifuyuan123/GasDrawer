package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.ListViewUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;
import com.sctjsj.gasdrawer.model.AuditModule;
import com.sctjsj.gasdrawer.presenter.adapter.AuditClAdapter;
import com.sctjsj.gasdrawer.presenter.adapter.AuditGreadAdapter;
import com.sctjsj.gasdrawer.ui.CustomView.CustomGridView;
import com.sctjsj.gasdrawer.ui.CustomView.ReasonDialog;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 审核界面
 */

@Route(path = "/main/act/AuditActivity")
public class AuditActivity extends BaseAppcompatActivity implements AuditModule.AuditModuleCallBAck {

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
    @BindView(R.id.audit_LtV_CL)
    ListView auditLtVCL;
    @BindView(R.id.audit_LtV_Ser)
    ListView auditLtVSer;
    @BindView(R.id.audit_GreadView)
    CustomGridView auditGreadView;
    @BindView(R.id.activity_audit)
    LinearLayout activityAudit;
    @BindView(R.id.audit_defult)
    Button auditDefult;
    @BindView(R.id.audit_succeed)
    Button auditSucceed;
    private AuditModule module;
    private ReasonDialog dialog;
    LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        module = new AuditModule(this, this);
        dialog = new ReasonDialog(this);
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        initView();
        dialog.setListener(new ReasonDialog.ResonDialogCallBack() {
            @Override
            public void onViewClicked(String data) {
                module.uuPass(bean.getId()+"", data);
            }
        });

    }

    //设置数据
    private void initView() {
        if (null != bean) {
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

            if(null!=bean.getMaterial()){
                List<ChildBean> data=bean.getMaterial();
                auditLtVCL.setVisibility(View.VISIBLE);
                auditLtVCL.addHeaderView(inflater.inflate(R.layout.audit_cl_head,null));
                AuditClAdapter aud= new AuditClAdapter(data,this);
                auditLtVCL.setAdapter(aud);
                ListViewUtil.setListViewHeightBasedOnChildren(auditLtVCL);
            }

            if(null!=bean.getServerChildBeen()){
                auditLtVSer.setVisibility(View.VISIBLE);
                auditLtVSer.addHeaderView(inflater.inflate(R.layout.audit_fw_head,null));
                auditLtVSer.setAdapter(new FwAdapter(bean.getServerChildBeen()));
                ListViewUtil.setListViewHeightBasedOnChildren(auditLtVSer);
            }
            if(null!=bean.getPicAddress()){
                auditGreadView.setVisibility(View.VISIBLE);
                auditGreadView.setNumColumns(3);
                AuditGreadAdapter adapter=new AuditGreadAdapter(this,bean.getPicAddress());
                auditGreadView.setAdapter(adapter);
                auditGreadView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     startActivity(new Intent(AuditActivity.this,ShowPicActivity.class).putExtra("pic", (Serializable) bean.getPicAddress()));
                    }
                });
            }
            Log.e("44444",bean.toString());

        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_audit;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.Instalact_Img_back, R.id.audit_defult, R.id.audit_succeed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Instalact_Img_back:
                finish();
                break;
            case R.id.audit_defult:
                //审核订单不通过
                dialog.show();
                break;
            case R.id.audit_succeed:
                //审核订单通过
                module.pass(bean.getId()+"");
                break;
        }
    }

    @Override
    public void succeed() {
        Toast.makeText(this, "审核成功!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void defult() {
        Toast.makeText(this, "审核失败，请联系管理员!", Toast.LENGTH_SHORT).show();
    }



     class FwAdapter extends BaseAdapter {
        List<ServerChildBean> data;

        public FwAdapter(List<ServerChildBean> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FwViewHolder holder ;
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.audit_fw_item, null);
                holder = new FwViewHolder();
                holder.auditFwTotalprice= (TextView) convertView.findViewById(R.id.audit_fw_totalprice);
                holder.auditFwName= (TextView) convertView.findViewById(R.id.audit_fw_name);
                holder.auditFwPrice= (TextView) convertView.findViewById(R.id.audit_fw_price);
                holder.auditFwCount= (TextView) convertView.findViewById(R.id.audit_fw_count);
                convertView.setTag(holder);
            } else {
                holder = (FwViewHolder) convertView.getTag();
            }
            holder.auditFwCount.setText(data.get(position).getCount()+"");
            holder.auditFwName.setText(data.get(position).getId()+"");
            holder.auditFwPrice.setText(data.get(position).getPrice()+"");
            holder.auditFwTotalprice.setText(data.get(position).getTotalPrice()+"");
            return convertView;
        }

        class FwViewHolder {

            TextView auditFwName;

            TextView auditFwPrice;

            TextView auditFwCount;

            TextView auditFwTotalprice;


        }
    }

}
