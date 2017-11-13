package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.ImageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShowPicActivity extends BaseAppcompatActivity {

    @BindView(R.id.indicator)
    TextView indicator;
    @BindView(R.id.vp)
    ViewPager vp;
    private List<ImageBean> data=new ArrayList<>();
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data= (List<ImageBean>) getIntent().getSerializableExtra("pic");
        initvp();

        if(data!=null){
            vp.setAdapter(adapter);
        }
    }

    private void initvp() {
        vp.setPageMargin(40);
        if(adapter==null){
            adapter=new PagerAdapter() {
                @Override
                public int getCount() {
                    return data==null?0:data.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view==object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, final int position) {
                    ImageView img=new ImageView(ShowPicActivity.this);
                    PicassoUtil.getPicassoObject().load(data.get(position).getUrl()).into(img);
                    container.addView(img);
                   return img;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);

                }
            };
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_show_pic;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
