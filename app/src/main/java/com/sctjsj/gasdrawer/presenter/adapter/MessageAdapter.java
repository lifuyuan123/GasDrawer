package com.sctjsj.gasdrawer.presenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.MessageBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuha on 2017/5/2.
 */

public class MessageAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private List<MessageBean> data;

    public MessageAdapter(Context mContext, List<MessageBean> data) {
        this.mContext = mContext;
        this.data = data;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        MessageViewHolder holder;
        if(null==convertView){
            convertView = inflater.inflate(R.layout.message_item, null);
            holder=new MessageViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (MessageViewHolder) convertView.getTag();
        }

        MessageBean bean=data.get(position);
        holder.messageItemTxt.setText(bean.getTitle());

        return convertView;
    }

    static class MessageViewHolder {
        @BindView(R.id.message_item_Img)
        ImageView messageItemImg;
        @BindView(R.id.message_item_Txt)
        TextView messageItemTxt;

        MessageViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
