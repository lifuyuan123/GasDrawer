package com.sctjsj.gasdrawer.presenter;

import com.sctjsj.gasdrawer.entity.ServicesItems;

import java.util.List;

/**
 * Created by liuha on 2017/4/27.
 */

public interface IChooseCostPresenter {
    public void init();
    public List<String> initGroupData();
    public List<List<ServicesItems>> initChildData();
}
