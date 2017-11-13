package com.sctjsj.gasdrawer.presenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.NoticeBean;

import java.util.List;

/**
 * Created by liuha on 2017/4/24.
 * 首页公告的适配器
 *
 */

public class HomeNoticeAdapter extends BaseAdapter {
    private Context mContext;
    private List<NoticeBean> data;
    private LayoutInflater mLayoutInflater;

    public HomeNoticeAdapter(Context mContext, List<NoticeBean> data) {
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
        ViewHolder mViewHolder;
        if(null==convertView){
            mViewHolder=new ViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.home_info_item,null);
            mViewHolder.notice= (TextView) convertView.findViewById(R.id.home_Notice_infoTxt);
            mViewHolder.time= (TextView) convertView.findViewById(R.id.home_Notice_time);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder= (ViewHolder) convertView.getTag();
        }
        NoticeBean notice = data.get(position);
        mViewHolder.notice.setText(notice.getTitle());
        mViewHolder.time.setText(notice.getPublishTime());
        return convertView;
    }

    static class  ViewHolder{
        TextView notice;
        TextView time;

    }

}
