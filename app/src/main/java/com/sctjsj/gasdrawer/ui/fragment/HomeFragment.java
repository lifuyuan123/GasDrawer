package com.sctjsj.gasdrawer.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.ui.frg.BaseFragment;
import com.sctjsj.basemodule.base.util.ListViewUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.NoticeBean;
import com.sctjsj.gasdrawer.event.GotoEvent;
import com.sctjsj.gasdrawer.presenter.IHomePresenter;
import com.sctjsj.gasdrawer.presenter.adapter.HomeNoticeAdapter;
import com.sctjsj.gasdrawer.presenter.impl.HomePresenterImpl;
import com.sctjsj.gasdrawer.ui.CustomView.MyDialog;
import com.sctjsj.gasdrawer.ui.view.IHomeFragment;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by liuha on 2017/4/21.
 * 首页的fragment
 */

public class HomeFragment extends BaseFragment implements IHomeFragment {

    @BindView(R.id.home_banner)
    Banner homeBanner;
    @BindView(R.id.home_more_Txt)
    TextView homeMoreTxt;
    @BindView(R.id.home_InFo_listView)
    ListView homeInFoListView;
    @BindView(R.id.home_ScroView)
    ScrollView homeScroView;
    Unbinder unbinder1;

    @BindView(R.id.home_instal_img)
    ImageView homeInstalImg;
    @BindView(R.id.home_weixiu_img)
    ImageView homeWeixiuImg;
    @BindView(R.id.home_sh_img)
    ImageView homeShImg;
    private IHomePresenter mIHomePresenter;
    private HomeNoticeAdapter mHomeNoticeAdapter;
    private int USERTYPE = 0;
    private MyDialog dialog;


    @Override
    protected int setLayoutResouceId() {
        return R.layout.layout_home;
    }

    @Override
    protected void initData(Bundle arguments) {
        USERTYPE = (int) SPFUtil.get(Tag.TAG_TYPE, 0);
        dialog = new MyDialog(getActivity());
        dialog.setTitle("提示");
        dialog.setMessage("您没有权限访问，请联系管理人员！");
        mIHomePresenter = new HomePresenterImpl(mActivity, this);
        mIHomePresenter.initBanner();
        //第一次进来的默认值
        if(USERTYPE==1||USERTYPE==4||USERTYPE==5||USERTYPE==7){
            SPFUtil.put(Tag.TAG_USERTYPE, 1);
        }else if(USERTYPE==2||USERTYPE==6){
            SPFUtil.put(Tag.TAG_USERTYPE, 2);
        }else if(USERTYPE == 3 ){
            SPFUtil.put(Tag.TAG_USERTYPE, 3);
        }

    }


    @Override
    public void setAdapter(List<NoticeBean> data) {
        if (data != null && data.size() > 0) {
            mHomeNoticeAdapter = new HomeNoticeAdapter(mActivity, data);
            homeInFoListView.setAdapter(mHomeNoticeAdapter);
            ListViewUtil.setListViewHeightBasedOnChildren(homeInFoListView);
        }
    }

    @Override
    public void getBannerData(List<String> data) {
        BannerImageLoader imageLoader = new BannerImageLoader();
        homeBanner.setImageLoader(imageLoader);
        homeBanner.setImages(data);
        homeBanner.setDelayTime(5000);
        homeBanner.start();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void setListener() {
        super.setListener();
        homeInFoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIHomePresenter.itemClick(position);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutResouceId(), container, false);
        unbinder1 = ButterKnife.bind(this, mRootView);
        initData(getArguments());
        initView();
        mIsPrepare = true;
        onLazyLoad();
        setListener();
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mIHomePresenter.initNoticeData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder1!=null)
        unbinder1.unbind();
    }

    @OnClick({R.id.home_instal_img, R.id.home_weixiu_img, R.id.home_sh_img,R.id.home_design_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_instal_img://安装
                if (USERTYPE == 1 || USERTYPE == 4 || USERTYPE == 5 || USERTYPE == 7) {
                    SPFUtil.put(Tag.TAG_USERTYPE, 1);
                    EventBus.getDefault().post(new GotoEvent());
                } else {
                    dialog.show();
                }

                break;
            case R.id.home_weixiu_img://维修
                if (USERTYPE == 2 || USERTYPE == 5 || USERTYPE == 6 || USERTYPE == 7) {
                    SPFUtil.put(Tag.TAG_USERTYPE, 2);
                    EventBus.getDefault().post(new GotoEvent());
                } else {
                    dialog.show();
                }


                break;
            case R.id.home_sh_img://审核
                if (USERTYPE == 3 || USERTYPE == 4 || USERTYPE == 6 || USERTYPE == 7) {
                    SPFUtil.put(Tag.TAG_USERTYPE, 3);
                    EventBus.getDefault().post(new GotoEvent());
                } else {
                    dialog.show();
                }
                break;
            case R.id.home_design_img:
                if (USERTYPE == 1 || USERTYPE == 4 || USERTYPE == 5 || USERTYPE == 7) {
                    SPFUtil.put(Tag.TAG_USERTYPE, 1);
                    ARouter.getInstance().build("/draw/act/drawtest").navigation();
                } else {
                    dialog.show();
                }
                break;

        }
    }



    //自定义的imageLoader
    public class BannerImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Drawable mDrawable = getActivity().getResources().getDrawable(R.drawable.banner_img);
            PicassoUtil.getPicassoObject().load((String) path).error(mDrawable).into(imageView);
        }
    }


}
