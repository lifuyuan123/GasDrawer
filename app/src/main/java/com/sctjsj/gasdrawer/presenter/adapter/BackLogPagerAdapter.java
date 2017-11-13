package com.sctjsj.gasdrawer.presenter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by liuha on 2017/4/26.
 * BackLog页面ViewPager的适配器
 */

public class BackLogPagerAdapter extends FragmentPagerAdapter {
    private List<String> title;//tabLayout的项
    private List<Fragment> fragments;
    public BackLogPagerAdapter(FragmentManager fm,List<String> title,List<Fragment> fragments) {
        super(fm);
        this.title=title;
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
