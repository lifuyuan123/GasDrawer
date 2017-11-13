package com.sctjsj.gasdrawer.presenter.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.ImageBean;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

/**
 * Created by liuha on 2017/6/3.
 */

public class AuditGreadAdapter extends BaseAdapter {
    private List<ImageBean> data;
    private Context mContext;
    private LayoutInflater inflater;

    public AuditGreadAdapter(Context mContext, List<ImageBean> data) {
        this.mContext = mContext;
        this.data = data;
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
        ViewHolder holder;
        if(null==convertView){
            convertView=inflater.inflate(R.layout.repair_picture_item,null);
            holder=new ViewHolder();
            holder.mImageView= (ImageView) convertView.findViewById(R.id.picture_item_img);
            holder.textView= (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        PicassoUtil.getPicassoObject().load(data.get(position).getUrl()).error(R.drawable.ic_adloading).into(holder.mImageView);
        if(!TextUtils.isEmpty(data.get(position).getTitle())&&data.get(position).getTitle()!=null){
            holder.textView.setText(data.get(position).getTitle());
            holder.textView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static  class ViewHolder{
        ImageView mImageView;
        TextView textView;
    }
}
