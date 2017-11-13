package com.sctjsj.gasdrawer.presenter.impl;

import android.content.Context;

import com.sctjsj.gasdrawer.entity.ServicesItems;
import com.sctjsj.gasdrawer.presenter.IChooseMaterialPresenter;
import com.sctjsj.gasdrawer.ui.view.IChooseCostActivity;
import com.sctjsj.gasdrawer.ui.view.IChooseMateriaActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuha on 2017/5/2.
 */

public class ChooseMaterialPresenterImpl implements IChooseMaterialPresenter{
    private List<String> groupData=new ArrayList<>();
    private List<List<ServicesItems>> childData=new ArrayList<>();
    private Context mContext;
    private IChooseMateriaActivity activity;

    public ChooseMaterialPresenterImpl(Context mContext, IChooseMateriaActivity activity) {
        this.mContext = mContext;
        this.activity = activity;
    }

    @Override
    public void init() {
        activity.setAdapter();
    }

    @Override
    public List<String> initGroupData() {
        groupData.add("水管类");
        groupData.add("弯头");
        groupData.add("燃气具类");
        groupData.add("地热管");
        groupData.add("其他");
        return groupData;
    }

    @Override
    public List<List<ServicesItems>> initChildData() {
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    List<ServicesItems> data = new ArrayList<>();
                    for (int j = 0; j < 7; j++) {
                        data.add(new ServicesItems("005" + (j + 4), "水管类" + j, "25", "我是其他的消息，描述水管类的", 0));
                    }
                    childData.add(data);
                    break;
                case 1:
                    List<ServicesItems> data1 = new ArrayList<>();
                    for (int j = 0; j < 5; j++) {
                        data1.add(new ServicesItems("005" + (j + 4), "弯头" + j, "25", "我是其他的消息，描述弯头类的", 0));
                    }
                    childData.add(data1);
                    break;
                case 2:
                    List<ServicesItems> data2 = new ArrayList<>();
                    for (int j = 0; j < 3; j++) {
                        data2.add(new ServicesItems("005" + (j + 4), "燃气具类" + j, "25", "我是其他的消息，描述燃气具类的", 0));
                    }
                    childData.add(data2);
                    break;
            }
        }
        return childData;
    }
}
