package com.sctjsj.gasdrawer.presenter;

import com.sctjsj.gasdrawer.entity.ServicesItems;

import java.util.List;

/**
 * Created by liuha on 2017/5/2.
 */

public interface IChooseMaterialPresenter {
    public void init();
    public List<String> initGroupData();
    public List<List<ServicesItems>> initChildData();
}
