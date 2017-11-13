package com.sctjsj.gasdrawer.presenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;

import java.util.List;

/**
 * Created by liuha on 2017/4/25.
 */

public class BackLogAdapter extends BaseAdapter {
    private Context mContext;
    private List<WorkMessageBean> data;
    private LayoutInflater mLayoutInflater;

    public List<WorkMessageBean> getData() {
        return data;
    }

    public void setData(List<WorkMessageBean> data) {
        this.data = data;
    }

    public BackLogAdapter(Context mContext, List<WorkMessageBean> data) {
        this.mContext = mContext;
        this.data = data;
        mLayoutInflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolder mViewHolder=null;
        if(null==convertView){
            mViewHolder=new ViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.backlog_item,null);
            mViewHolder.mTextView= (TextView) convertView.findViewById(R.id.backlog_item_Txt);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder= (ViewHolder) convertView.getTag();
        }
        WorkMessageBean bean=data.get(position);
            mViewHolder.mTextView.setText(bean.getAddress()+" "+bean.getClientName());
        return convertView;
    }

    static  class  ViewHolder{
        TextView mTextView;
    }
}
