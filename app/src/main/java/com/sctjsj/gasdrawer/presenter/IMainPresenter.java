package com.sctjsj.gasdrawer.presenter;

/**
 * Created by liuha on 2017/4/21.
 */

public interface IMainPresenter {
    /**
     * 加载fragment的方法
     */
    public void addFragment();

    /**
     * 替换fragment的方法
     * @param name
     */
    public void replaceFragment(String name);


    public void ClickImg();

}
