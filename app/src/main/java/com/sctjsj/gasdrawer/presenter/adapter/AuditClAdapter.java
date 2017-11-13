package com.sctjsj.gasdrawer.presenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ChildBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuha on 2017/6/2.
 */

public class AuditClAdapter extends BaseAdapter {
        private List<ChildBean> data;
        private LayoutInflater inflater;
        private Context mContext;


        public AuditClAdapter(List<ChildBean> data,Context mContext) {
            this.data = data;
            this.mContext=mContext;
            this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
            ClViewHolder holder ;
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.audit_cl_item, null);
                holder = new ClViewHolder();
                holder.auditClName= (TextView) convertView.findViewById(R.id.audit_cl_name);
                holder.auditClPrice= (TextView) convertView.findViewById(R.id.audit_cl_price);
                holder.auditClCount= (TextView) convertView.findViewById(R.id.audit_cl_count);
                holder.auditClTotalprice= (TextView) convertView.findViewById(R.id.audit_cl_totalprice);
                convertView.setTag(holder);
            } else {
                holder = (ClViewHolder) convertView.getTag();
            }
            holder.auditClCount.setText(data.get(position).getCount()+"");
            holder.auditClName.setText(data.get(position).getId()+"");
            holder.auditClPrice.setText(data.get(position).getUnit()+"");
            holder.auditClTotalprice.setText(data.get(position).getEditext()+"");
            return convertView;
        }

        class ClViewHolder {

            TextView auditClName;
            TextView auditClPrice;
            TextView auditClCount;
            TextView auditClTotalprice;

        }
    }

