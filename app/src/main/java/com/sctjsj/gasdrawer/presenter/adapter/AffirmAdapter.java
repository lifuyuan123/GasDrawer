package com.sctjsj.gasdrawer.presenter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sctjsj.gasdrawer.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuha on 2017/5/5.
 * 安装确认单照片适配器
 */

public class AffirmAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Bitmap> data;
    private Context mContext;

    public AffirmAdapter(List<Bitmap> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
        inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        AffirmAdapterHolder holder;
        if(null==convertView){
            convertView = inflater.inflate(R.layout.repair_picture_item, null);
            holder=new AffirmAdapterHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (AffirmAdapterHolder) convertView.getTag();
        }
        holder.pictureItemImg.setImageBitmap(data.get(position));
        return convertView;
    }

    static class AffirmAdapterHolder {
        @BindView(R.id.picture_item_img)
        ImageView pictureItemImg;

        AffirmAdapterHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
