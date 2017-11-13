package com.sctjsj.gasdrawer.presenter.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.GroupBean;
import com.sctjsj.gasdrawer.entity.ServicesItems;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.ui.CustomView.MyDialog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuha on 2017/4/27.
 */

public class ChooseCostAdapter extends BaseExpandableListAdapter {
    private List<GroupBean> groupData;
    private List<List<ServerChildBean>> childData;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Set<ServerChildBean> serverChildBeen=new HashSet<>();

    public ChooseCostAdapter(List<GroupBean> groupData, List<List<ServerChildBean>> childData, Context mContext) {
        this.groupData = groupData;
        this.childData = childData;
        this.mContext = mContext;
        mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(groupPosition).size()+1 ;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }



    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupViewHolder mGroupViewHolder = null;
        if(null==convertView){
            convertView = mLayoutInflater.inflate(R.layout.chost_group_item, null);
            mGroupViewHolder=new GroupViewHolder(convertView);
            convertView.setTag(mGroupViewHolder);
        }else {
            mGroupViewHolder= (GroupViewHolder) convertView.getTag();
        }
        //加载数据
        if(isExpanded){//父容器已经被打开
            mGroupViewHolder.chostGroupItemImg.setImageResource(R.drawable.buttom_icon);
        }else {//父容器没有被打开
            mGroupViewHolder.chostGroupItemImg.setImageResource(R.drawable.right_icon);
        }
        mGroupViewHolder.chostGroupItemTxt.setText(groupData.get(groupPosition).getClassName());

        return convertView;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if(childPosition==0){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public int getChildTypeCount() {
        return 10;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
           int type= getChildType(groupPosition,childPosition);
           final ChildViewHolder mChildViewHolder;
           if(type==1){
               if(null==convertView){
                   convertView=mLayoutInflater.inflate(R.layout.chost_child_head,null);
                   TextView view= (TextView) convertView.findViewById(R.id.tv_unit);
                   view.setText("单位");
               }
           }else {
               if(convertView==null){
                   convertView = LayoutInflater.from(mContext).inflate(R.layout.chose_child_items,null);
                   mChildViewHolder=new ChildViewHolder(convertView);
                   convertView.setTag(mChildViewHolder);
               }else {
                   mChildViewHolder= (ChildViewHolder) convertView.getTag();
               }

               final ServerChildBean item = childData.get(groupPosition).get(childPosition-1);
               mChildViewHolder.choseChildCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       if(isChecked){
                           item.setType(1);
                       }else {
                           item.setType(0);
                       }
                       notifyDataSetChanged();
                   }
               });

               //绑定输入的item
               mChildViewHolder.choseChildEdt.setTag(item);
               //清除焦点
//               mChildViewHolder.choseChildEdt.clearFocus();
               //输入监听
               mChildViewHolder.choseChildEdt.addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                   }
                   @Override
                   public void onTextChanged(CharSequence s, int start, int before, int count) {
                       //获取输入的item
                       ServerChildBean item= (ServerChildBean) mChildViewHolder.choseChildEdt.getTag();
                       item.setEditext(s.toString());
                   }
                   @Override
                   public void afterTextChanged(Editable s) {
                   }
               });
               //设置editext
               if(!TextUtils.isEmpty(item.getEditext())){
                   mChildViewHolder.choseChildEdt.setText(item.getEditext());
               }else{
                   mChildViewHolder.choseChildEdt.setText("");
               }

               Log.e("-----",item.toString());
               if(item.getType()!=0){
                   mChildViewHolder.choseChildCheck.setChecked(true);
                   serverChildBeen.add(item);
               }else {
                   mChildViewHolder.choseChildCheck.setChecked(false);
                   serverChildBeen.remove(item);
               }
               mChildViewHolder.choseChildSerNumber.setText(item.getUnit());
               mChildViewHolder.choseChildSerName.setText(item.getProName());
               mChildViewHolder.choseChildCost.setText(item.getPrice()+"");
               Log.e("dfaasdf",item.getPrice()+"");
               mChildViewHolder.choseChildSerMsg.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       MyDialog dialog=new MyDialog(mContext);
                       dialog.setTitle("备注");
                       dialog.setMessage(item.getRemarks());
                       dialog.show();
                   }
               });
           }
        return convertView;

    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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
    //父item的ViewHolder
    static class GroupViewHolder {
        @BindView(R.id.chost_group_item_Txt)
        TextView chostGroupItemTxt;
        @BindView(R.id.chost_group_item_Img)
        ImageView chostGroupItemImg;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

     //获取选中的数据集合
    public Set<ServerChildBean> getServerChildBeen(){
        return serverChildBeen;
    }







}
