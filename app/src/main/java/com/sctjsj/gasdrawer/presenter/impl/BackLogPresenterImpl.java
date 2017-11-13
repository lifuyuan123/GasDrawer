package com.sctjsj.gasdrawer.presenter.impl;

import android.content.Context;
import android.os.Bundle;

import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.gasdrawer.application.MyApp;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.presenter.IBackLogPresenter;
import com.sctjsj.gasdrawer.ui.fragment.WaitAuditFragment;
import com.sctjsj.gasdrawer.ui.fragment.WaitDisposeFragment;
import com.sctjsj.gasdrawer.ui.fragment.WaitUploadFragment;
import com.sctjsj.gasdrawer.ui.view.IBackLogFragement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuha on 2017/4/25.
 */

public class BackLogPresenterImpl implements IBackLogPresenter {
    private Context mContext;
    private IBackLogFragement mIBackLogFragement;
    private Map<String,List<?>> data;
    private List<String> title=new ArrayList<>();
    private List fragments=new ArrayList();
    private int USERTYPE;




    public BackLogPresenterImpl(Context mContext, IBackLogFragement mIBackLogFragement) {
        this.mContext = mContext;
        this.mIBackLogFragement = mIBackLogFragement;

    }


    @Override
    public Map<String, List<?>> initdata() {
        title.clear();
        fragments.clear();
        USERTYPE= (int) SPFUtil.get(Tag.TAG_TYPE,0);
        data=new HashMap<>();
        if((int)SPFUtil.get(Tag.TAG_USERTYPE,0)!=0){
            USERTYPE=(int)SPFUtil.get(Tag.TAG_USERTYPE,0);
        }

        //读取本地用户的信息
        switch (USERTYPE){
            case 1://安装
                title.add("待安装");
                WaitDisposeFragment disposeFragment=new WaitDisposeFragment();
                Bundle mBundle=new Bundle();
                mBundle.putInt("key",1);
                disposeFragment.setArguments(mBundle);
                fragments.add(disposeFragment);
                title.add("待上传");
                fragments.add(new WaitUploadFragment());
                break;
            case 2://维修
                title.add("待维修");
                WaitDisposeFragment dis=new WaitDisposeFragment();
                Bundle dle=new Bundle();
                dle.putInt("key",2);
                dis.setArguments(dle);
                fragments.add(dis);
                break;
            case 3://审核
                title.add("待审核");
                fragments.add(new WaitAuditFragment());
                break;
            case 5:
                title.add("待安装");
                WaitDisposeFragment disposeFragment2=new WaitDisposeFragment();
                Bundle mBundle2=new Bundle();
                mBundle2.putInt("key",1);
                disposeFragment2.setArguments(mBundle2);
                fragments.add(disposeFragment2);
                title.add("待上传");
                fragments.add(new WaitUploadFragment());
                break;
            case 6:
                title.add("待审核");
                fragments.add(new WaitAuditFragment());
                break;
            case 7:
                title.add("待安装");
                WaitDisposeFragment disposeFragment1=new WaitDisposeFragment();
                Bundle mBundle1=new Bundle();
                mBundle1.putInt("key",1);
                disposeFragment1.setArguments(mBundle1);
                fragments.add(disposeFragment1);
                title.add("待上传");
                fragments.add(new WaitUploadFragment());
                break;
        }
        data.put("title",title);
        data.put("fragment",fragments);
        return data;
    }

    @Override
    public void init() {
        mIBackLogFragement.setAdapter();
    }
}
