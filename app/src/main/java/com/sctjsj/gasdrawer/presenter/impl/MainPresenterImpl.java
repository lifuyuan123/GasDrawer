package com.sctjsj.gasdrawer.presenter.impl;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.presenter.IMainPresenter;
import com.sctjsj.gasdrawer.ui.fragment.BackLogFragment;
import com.sctjsj.gasdrawer.ui.fragment.HomeFragment;
import com.sctjsj.gasdrawer.ui.fragment.MessageFragment;
import com.sctjsj.gasdrawer.ui.fragment.MineFragment;

/**
 * Created by liuha on 2017/4/21.
 */

public class MainPresenterImpl implements IMainPresenter {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private  FragmentTransaction mFragmentTransaction;
    private HomeFragment mHomeFragment;
    private BackLogFragment mBackLogFragment;
    private MessageFragment mMessageFragment;
    private MineFragment mMineFragment;



    public MainPresenterImpl(Context mContext, FragmentManager mFragmentManager) {
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;

    }

    @Override
    public void addFragment() {
        replaceFragment("home");
    }


    @Override
    public void replaceFragment(String name) {
        hideAllFgIfNotNull();
        mFragmentTransaction= mFragmentManager.beginTransaction();
        if(name.equals("home")){
            if(mHomeFragment==null){
                mHomeFragment=new HomeFragment();
                mFragmentTransaction.add(R.id.replaceId,mHomeFragment);
            }else {
                mFragmentTransaction.show(mHomeFragment);
            }
        }else if(name.equals("backlog")){
            if(mBackLogFragment==null){
                mBackLogFragment=new BackLogFragment();
                mFragmentTransaction.add(R.id.replaceId,mBackLogFragment);
            }else {
                mFragmentTransaction.show(mBackLogFragment);
            }
        }else if (name.equals("message")){
            if(mMessageFragment==null){
                mMessageFragment=new MessageFragment();
                mFragmentTransaction.add(R.id.replaceId,mMessageFragment);
            }else {
                mFragmentTransaction.show(mMessageFragment);
            }
        }else if (name.equals("mine")){
            if(mMineFragment==null){
                mMineFragment=new MineFragment();
                mFragmentTransaction.add(R.id.replaceId,mMineFragment);
            }else {
                mFragmentTransaction.show(mMineFragment);
            }
        }
        mFragmentTransaction.commit();
    }

    //隐藏所有的fragment
    private void hideAllFgIfNotNull() {
        mFragmentTransaction= mFragmentManager.beginTransaction();
        if(null!=mHomeFragment){
            mFragmentTransaction.hide(mHomeFragment);
        }
        if(null!=mBackLogFragment){
            mFragmentTransaction.hide(mBackLogFragment);
        }
        if(null!=mMessageFragment){
            mFragmentTransaction.hide(mMessageFragment);
        }
        if(null!=mMineFragment){
            mFragmentTransaction.hide(mMineFragment);
        }
        mFragmentTransaction.commit();
    }

    @Override
    public void ClickImg() {
        hideAllFgIfNotNull();
        if(null==mBackLogFragment){
            mFragmentTransaction=mFragmentManager.beginTransaction();
            mBackLogFragment=new BackLogFragment();
            mFragmentTransaction.add(R.id.replaceId,mBackLogFragment).show(mBackLogFragment);
        }else {
            mFragmentTransaction=mFragmentManager.beginTransaction();
            mFragmentTransaction.remove(mBackLogFragment);
            mBackLogFragment=new BackLogFragment();
            mFragmentTransaction.add(R.id.replaceId,mBackLogFragment).show(mBackLogFragment);
        }
        mFragmentTransaction.commit();
    }
}
