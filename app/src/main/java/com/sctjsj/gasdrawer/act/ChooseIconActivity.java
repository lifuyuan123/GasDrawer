package com.sctjsj.gasdrawer.act;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;

import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ICON;
import com.sctjsj.gasdrawer.event.ChooseEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Route(path = "/main/act/choose_icon")
public class ChooseIconActivity extends BaseAppcompatActivity {
    @BindView(R.id.rv)RecyclerView mRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRV();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_choose_icon;
    }

    @Override
    public void reloadData() {

    }
    private void initRV(){
        List<ICON> list=new ArrayList<>();
        list.add(new ICON(1,"表具",R.mipmap.ic_launcher));
        list.add(new ICON(1,"测压阀",R.mipmap.ic_launcher));
        list.add(new ICON(1,"出入墙套盒",R.mipmap.ic_launcher));
        list.add(new ICON(1,"穿墙",R.mipmap.ic_launcher));
        list.add(new ICON(1,"电磁阀",R.mipmap.ic_launcher));
        list.add(new ICON(1,"管道延伸",R.mipmap.ic_launcher));
        list.add(new ICON(1,"气嘴",R.mipmap.ic_launcher));
        list.add(new ICON(1,"球阀",R.mipmap.ic_launcher));

        mRV.setAdapter(new MyAdapter(list,this));
        mRV.setLayoutManager(new GridLayoutManager(this,3));


    }

    class MyAdapter extends RecyclerView.Adapter<XHolder>{
        List<ICON>data;
        Context context;
        public MyAdapter(List<ICON> list, Context con){
            this.data=list;
            this.context=con;
        }

        @Override
        public XHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gv_icon,null);
            XHolder holder=new XHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(XHolder holder, final int position) {
            holder.mIVIcon.setImageResource(data.get(position).getResId());
            holder.mTVName.setText(data.get(position).getName());
            holder.mLLParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new ChooseEvent(data.get(position).getResId()));
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
    class XHolder extends RecyclerView.ViewHolder{
        ImageView mIVIcon;
        TextView mTVName;
        LinearLayout mLLParent;
        public XHolder(View itemView) {
            super(itemView);
            mIVIcon= (ImageView) itemView.findViewById(R.id.item_gv_icon_iv_icon);
            mTVName= (TextView) itemView.findViewById(R.id.item_gv_icon_tv_name);
            mLLParent= (LinearLayout) itemView.findViewById(R.id.item_gv_icon_ll_parent);
        }
    }

}
