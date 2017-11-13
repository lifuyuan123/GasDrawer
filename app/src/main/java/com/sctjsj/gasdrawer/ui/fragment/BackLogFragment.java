package com.sctjsj.gasdrawer.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sctjsj.basemodule.base.ui.frg.BaseFragment;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.presenter.IBackLogPresenter;
import com.sctjsj.gasdrawer.presenter.adapter.BackLogPagerAdapter;
import com.sctjsj.gasdrawer.presenter.impl.BackLogPresenterImpl;
import com.sctjsj.gasdrawer.ui.view.IBackLogFragement;

import java.util.List;
import java.util.Map;


/**
 * Created by liuha on 2017/4/21.
 */

public class BackLogFragment extends BaseFragment implements IBackLogFragement {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private IBackLogPresenter mIBackLogPresenter;
    private Map<String, List<?>> data;
    private BackLogPagerAdapter adapter;
    private LinearLayout layout;
    private int USERTYPE;
    private TextView mTextView;



    @Override
    protected int setLayoutResouceId() {
        return R.layout.layout_backlog;
    }

    @Override
    protected void initData(Bundle arguments) {
        if((int)SPFUtil.get(Tag.TAG_USERTYPE,0)!=0){
            USERTYPE=(int)SPFUtil.get(Tag.TAG_USERTYPE,0);
        }else {
            USERTYPE= (int) SPFUtil.get(Tag.TAG_TYPE,0);
        }
        mIBackLogPresenter=new BackLogPresenterImpl(mActivity,this);
        data = mIBackLogPresenter.initdata();

    }

    @Override
    protected void initView() {
        findView();
        mTabLayout.setupWithViewPager(mViewPager);//将导航栏和ViewPager进行关联
        setListeners();
        adapter=new BackLogPagerAdapter(getChildFragmentManager(),(List<String>) data.get("title"),(List<Fragment>) data.get("fragment"));
        mIBackLogPresenter.init();
    }
    //设置tabLayout的监听
    private void setListeners() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());//联动
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //查找控件
    private void findView() {
        mTabLayout= (TabLayout) mRootView.findViewById(R.id.backlog_Table);
        mViewPager= (ViewPager) mRootView.findViewById(R.id.backlog_ViewPager);
        layout= (LinearLayout) mRootView.findViewById(R.id.backlog_layout);
        mTextView= (TextView) mRootView.findViewById(R.id.backLog_txt_title);
        if(USERTYPE==2){
            layout.setVisibility(View.VISIBLE);
            mTabLayout.setVisibility(View.GONE);
            mTextView.setText("待维修");
        }else if(USERTYPE==3||USERTYPE==6){
            layout.setVisibility(View.VISIBLE);
            mTabLayout.setVisibility(View.GONE);
            mTextView.setText("待审核");
        }else {
            layout.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onLazyLoad() {

    }

    @Override
    public void onStart() {
       super.onStart();
        data = mIBackLogPresenter.initdata();
        //adapter=new BackLogPagerAdapter(getChildFragmentManager(),(List<String>) data.get("title"),(List<Fragment>) data.get("fragment"));
        //mViewPager.setAdapter(adapter);
        //adapter.notify();
    }

    @Override
    public void setAdapter() {
            mViewPager.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(USERTYPE==2){
            layout.setVisibility(View.VISIBLE);
            mTabLayout.setVisibility(View.GONE);
            mTextView.setText("待维修");
        }else if(USERTYPE==3||USERTYPE==6){
            layout.setVisibility(View.VISIBLE);
            mTabLayout.setVisibility(View.GONE);
            mTextView.setText("待审核");
        }else {
            layout.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.VISIBLE);
        }
    }
}
