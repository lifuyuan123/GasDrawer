package com.sctjsj.gasdrawer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sctjsj.basemodule.base.ui.frg.BaseFragment;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.event.GotoEvent;
import com.sctjsj.gasdrawer.presenter.IMessagePresenter;
import com.sctjsj.gasdrawer.presenter.adapter.MessageAdapter;
import com.sctjsj.gasdrawer.presenter.impl.MessagePresenterImpl;
import com.sctjsj.gasdrawer.ui.view.IMessageFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by liuha on 2017/4/21.
 * 消息的fragment
 */

public class MessageFragment extends BaseFragment implements IMessageFragment{
    @BindView(R.id.message_listView)
    ListView messageListView;

    private IMessagePresenter presenter;
    private MessageAdapter adapter;
    Unbinder unbinder;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.layout_message;
    }

    @Override
    protected void initData(Bundle arguments) {
        presenter=new MessagePresenterImpl(mActivity,this);
        presenter.initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.initData();

    }

    @Override
    protected void initView() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //1.加载布局 XML 文件
        mRootView = inflater.inflate(setLayoutResouceId(), container, false);
        //2.接收从别的地方传过来的参数，如 Intent 跳转
        unbinder=ButterKnife.bind(this,mRootView);
        initData(getArguments());
        //3.绑定 Fragment中的控件
        initView();
        //4.设置碎片已加载完
        mIsPrepare = true;
        //5.懒加载数据
        onLazyLoad();
        //6.设置监听
        setListener();

        return mRootView;
    }

    @Override
    protected void onLazyLoad() {

    }



    @Override
    public void setAdapter(List data) {
        if(null!=data&&data.size()>0){
            adapter=new MessageAdapter(mActivity,data);
            messageListView.setAdapter(adapter);
            messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //跳转到代办
//                    Toast.makeText(mActivity, "aaaa", Toast.LENGTH_SHORT).show();
//                    SPFUtil.put(Tag.TAG_USERTYPE,1);
                    EventBus.getDefault().post(new GotoEvent());
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
