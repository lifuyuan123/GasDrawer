package com.sctjsj.gasdrawer.ui.view;

import android.provider.MediaStore;

import com.sctjsj.gasdrawer.entity.javaBean.NoticeBean;

import java.util.List;

/**
 * Created by liuha on 2017/4/24.
 */

public interface IHomeFragment {
    public void setAdapter(List<NoticeBean> data);
    public void getBannerData(List<String> data);

}
