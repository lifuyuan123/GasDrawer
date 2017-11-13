package com.sctjsj.gasdrawer.presenter;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by liuha on 2017/4/24.
 */

public interface IHomePresenter {


    /**
     * 初始化banner
     * @return
     */

    public void initBanner();

    /**
     * 初始化公告数据
     * @return
     */
    public void initNoticeData();

    /***
     * 回掉监听事件
     * @param position
     */

    public void itemClick(int position);
}
