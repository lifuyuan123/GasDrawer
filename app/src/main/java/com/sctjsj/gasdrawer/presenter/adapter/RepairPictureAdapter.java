package com.sctjsj.gasdrawer.presenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.ui.activity.AuditActivity;
import com.sctjsj.gasdrawer.ui.activity.ShowPicActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuha on 2017/5/5.
 *选择照片界面的适配器
 */

public class RepairPictureAdapter extends RecyclerView.Adapter<RepairPictureAdapter.RepairPictureHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    private List<Bitmap> data;
    private AddPictureCallBack callBack;
    private Longclick longclick;

    public RepairPictureAdapter(Context mContext, List<Bitmap> data) {
        this.mContext = mContext;
        this.data = data;
        inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RepairPictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View mView=inflater.inflate(R.layout.repair_picture_item,null);
        RepairPictureHolder holder=new RepairPictureHolder(mView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RepairPictureHolder holder, final int position) {
        holder.mImageView.setImageBitmap(data.get(position));
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(data.size()-1==position&&null!=callBack){
                   //执行添加照片
                   callBack.addPicture();
               }
//               if (data.size()>1&&callBack!=null){
//                   List<Bitmap> list=new ArrayList<Bitmap>();
//                   list.addAll(data);
//                   list.remove(data.size()-1);
//                   mContext.startActivity(new Intent(mContext,ShowPicActivity.class).putExtra("pic", (Serializable) list));;
//               }
            }
        });

        holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(data.size()-1!=position&&longclick!=null){
                    longclick.movePicture(position);
                    notifyDataSetChanged();
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class  RepairPictureHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        public RepairPictureHolder(View itemView) {
            super(itemView);
            mImageView= (ImageView) itemView.findViewById(R.id.picture_item_img);
        }

    }

    public interface AddPictureCallBack{
        public void addPicture();
    }
    public void setAddCallBack(AddPictureCallBack callBack){
        this.callBack=callBack;
    }

    public interface Longclick{
        public void movePicture(int position);
    }

    public void setLongclick(Longclick longclick) {
        this.longclick = longclick;
    }
}
