package com.sctjsj.gasdrawer.presenter.impl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.NoticeBean;
import com.sctjsj.gasdrawer.model.HomeModule;
import com.sctjsj.gasdrawer.presenter.IHomePresenter;
import com.sctjsj.gasdrawer.ui.view.IHomeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuha on 2017/4/24.
 */

public class HomePresenterImpl implements IHomePresenter,HomeModule.HomeCallBack{
    private Context mContext;
    private List<Drawable> bannerData;
    private IHomeFragment mIHomeFragment;
    private List<NoticeBean> noticeData;
    private HomeModule module=new HomeModule(this);

    public HomePresenterImpl(Context mContext, IHomeFragment mIHomeFragment) {
        this.mContext = mContext;
        this.mIHomeFragment = mIHomeFragment;
    }

    @Override
    public void initBanner() {
        module.getBannerData();
    }
    //获取通知数据
    @Override
    public void initNoticeData() {
        module.getNoticeMsg();
    }


    @Override
    public void itemClick(int position) {
        ARouter.getInstance().build("/main/act/HomeNoticeActivity").withObject("data",noticeData.get(position)).navigation();
    }

    //设置公告数据
    @Override
    public void Succeed(List data) {
        if(null!=data&&data.size()>0){
            noticeData=data;
            mIHomeFragment.setAdapter(data);
        }

    }
    //设置banner数据
    @Override
    public void bannerSucced(List data) {
        if(null!=data&&data.size()>0){
           mIHomeFragment.getBannerData(data);
            Log.e("--------",data.toString()+"");
        }
    }
}
