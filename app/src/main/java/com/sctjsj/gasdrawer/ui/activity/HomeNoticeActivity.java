package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.NoticeBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * home页面的公告详情页面
 */
@Route(path = "/main/act/HomeNoticeActivity")
public class HomeNoticeActivity extends BaseAppcompatActivity {

    @BindView(R.id.Home_NoticeInFo_back)
    ImageView HomeNoticeInFoBack;
    @BindView(R.id.Home_NoticeInFo_titleTxt)
    TextView HomeNoticeInFoTitleTxt;
    @BindView(R.id.Home_NoticeInFo_dateTxt)
    TextView HomeNoticeInFoDateTxt;
    @BindView(R.id.Home_NoticeInfo_bodyTxt)
    TextView HomeNoticeInfoBodyTxt;
    private Unbinder bind;
    @Autowired(name = "data")
    NoticeBean mNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ButterKnife.bind(this);
        HomeNoticeInFoTitleTxt.setText(mNotice.getTitle());
        HomeNoticeInFoDateTxt.setText(mNotice.getPublishTime());
        HomeNoticeInfoBodyTxt.setText(mNotice.getContent());

    }

    @Override
    public int initLayout() {
        return R.layout.activity_home_notice;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.Home_NoticeInFo_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

}
