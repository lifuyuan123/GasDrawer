package com.sctjsj.gasdrawer.presenter.impl;

import android.content.Context;

import com.sctjsj.gasdrawer.entity.javaBean.MessageBean;
import com.sctjsj.gasdrawer.model.MessageModule;
import com.sctjsj.gasdrawer.presenter.IMessagePresenter;
import com.sctjsj.gasdrawer.ui.view.IMessageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuha on 2017/5/2.
 * 消息界面的业务逻辑
 */

public class MessagePresenterImpl implements IMessagePresenter,MessageModule.MessageModuleCallBack{
    private Context mContext;
    private IMessageFragment fragment;
    private List<MessageBean> data=new ArrayList<>();
    private MessageModule module;

    public MessagePresenterImpl(Context mContext, IMessageFragment fragment) {
        this.mContext = mContext;
        this.fragment = fragment;
    }



    @Override
    public void initData() {
        module=new MessageModule(mContext,this);
        module.getMessageData();
    }



    @Override
    public void succeed(List data) {
        this.data=data;
        fragment.setAdapter(data);
    }


}
