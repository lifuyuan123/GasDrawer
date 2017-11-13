package com.sctjsj.gasdrawer.presenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ChildBean;

import java.util.List;

/**
 * Created by lifuy on 2017/5/26.
 */

public class SelectMaterialAdapter extends BaseAdapter {
    private List<ChildBean> list;
    private Context context;

    public SelectMaterialAdapter(List<ChildBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getCount() {
        return list.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder=null;
        if(convertView==null){
            if(getItemViewType(position)==0){
                convertView=LayoutInflater.from(context).inflate(R.layout.select_maaterial_top, null);
            }else if (getItemViewType(position)==1){
                convertView= LayoutInflater.from(context).inflate(R.layout.select_material,parent,false);
                holder=new MyViewHolder();
                holder.all= (TextView) convertView.findViewById(R.id.tv_all);
                holder.num= (TextView) convertView.findViewById(R.id.tv_num);
                holder.price= (TextView) convertView.findViewById(R.id.tv_price);
                holder.name= (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            }

        }else {
            if(getItemViewType(position)==1)
            holder = (MyViewHolder) convertView.getTag();
        }
        if(getItemViewType(position)==1){
            ChildBean bean=list.get(position-1);
            holder.all.setText(bean.getAll()+"");
            holder.name.setText(bean.getMaterialName());
            holder.price.setText(bean.getSellingPrice()+"");
            holder.num.setText(bean.getUnit());
        }

        return convertView;
    }

    class MyViewHolder{
        TextView name,price,num,all;

    }
}
