package com.sctjsj.gasdrawer.presenter.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.ui.CustomView.MyDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lifuy on 2017/6/7.
 */

public class SearchServerAdapter extends BaseAdapter {
    private Set<ServerChildBean> childlist=new HashSet<>();
    private List<ServerChildBean> list=new ArrayList<>();
    private Context context;

    public SearchServerAdapter(List<ServerChildBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public int getCount() {
        return list.size();
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
        ChildViewHolder mChildViewHolder=null;
        if(getItemViewType(position)==1){
            if (convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.chost_child_head,null);
            }
        }else {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.chose_child_items, null);
                mChildViewHolder = new ChildViewHolder(convertView);
                convertView.setTag(mChildViewHolder);
            } else {
                mChildViewHolder = (ChildViewHolder) convertView.getTag();
            }

            final ServerChildBean item = list.get(position - 1);
            mChildViewHolder.choseChildCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        item.setType(1);
                    } else {
                        item.setType(0);
                    }
                    notifyDataSetChanged();
                }
            });

            //绑定输入的item
            mChildViewHolder.choseChildEdt.setTag(item);
            //清除焦点
            mChildViewHolder.choseChildEdt.clearFocus();
            //输入监听
            final ChildViewHolder finalMChildViewHolder = mChildViewHolder;
            mChildViewHolder.choseChildEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //获取输入的item
                    ServerChildBean item = (ServerChildBean) finalMChildViewHolder.choseChildEdt.getTag();
                    item.setEditext(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            //设置editext
            if (!TextUtils.isEmpty(item.getEditext())) {
                mChildViewHolder.choseChildEdt.setText(item.getEditext());
            } else {
                mChildViewHolder.choseChildEdt.setText("");
            }

            Log.e("-----", item.toString());
            if (item.getType() != 0) {
                mChildViewHolder.choseChildCheck.setChecked(true);
                childlist.add(item);
            } else {
                mChildViewHolder.choseChildCheck.setChecked(false);
                childlist.remove(item);
            }
            mChildViewHolder.choseChildSerNumber.setText(item.getUnit());
            mChildViewHolder.choseChildSerName.setText(item.getProName());
            mChildViewHolder.choseChildCost.setText(item.getPrice() + "");
            mChildViewHolder.choseChildSerMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDialog dialog = new MyDialog(context);
                    dialog.setTitle("备注");
                    dialog.setMessage(item.getRemarks());
                    dialog.show();
                }
            });
        }
        return convertView;
    }

    //子item的ViewHolder
    static class ChildViewHolder {
        @BindView(R.id.chose_child_check)
        CheckBox choseChildCheck;
        @BindView(R.id.chose_child_SerNumber)
        TextView choseChildSerNumber;
        @BindView(R.id.chose_child_SerName)
        TextView choseChildSerName;
        @BindView(R.id.chose_child_Cost)
        TextView choseChildCost;
        @BindView(R.id.chose_child_SerMsg)
        Button choseChildSerMsg;
        @BindView(R.id.chose_child_edt)
        EditText choseChildEdt;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //获取选中的数据集合
    public Set<ServerChildBean> getServerChildBeen(){
        return childlist;
    }
}
