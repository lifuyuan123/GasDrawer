package com.sctjsj.gasdrawer.presenter.impl;

import android.content.Context;

import com.sctjsj.gasdrawer.entity.ServicesItems;
import com.sctjsj.gasdrawer.presenter.IChooseCostPresenter;
import com.sctjsj.gasdrawer.ui.view.IChooseCostActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuha on 2017/4/27.
 */

public class ChooseCostPresenter implements IChooseCostPresenter {
    private List<String> groupData =new ArrayList<>();
    private List<List<ServicesItems>> childData=new ArrayList<>();
    private Context mContext;
    private IChooseCostActivity mIChooseCostActivity;

    public ChooseCostPresenter(Context mContext, IChooseCostActivity mIChooseCostActivity) {
        this.mContext = mContext;
        this.mIChooseCostActivity = mIChooseCostActivity;
    }

    @Override
    public void init() {
        mIChooseCostActivity.setAdapter();

    }

    @Override
    public List<String> initGroupData() {
        groupData.add("燃气具");
        groupData.add("计量表");
        groupData.add("居民用气维修");
        groupData.add("居民用气户内改造");
        groupData.add("安全装置");
        groupData.add("其他");
        return groupData;
    }

    @Override
    public List<List<ServicesItems>> initChildData() {
        for (int i = 0; i <6 ; i++) {
            switch (i){
                case 0:
                    List<ServicesItems> data=new ArrayList<>();
                    for (int j = 0; j <7 ; j++) {
                        data.add(new ServicesItems("005"+(i+4),"燃气具类型"+i,"25","我是其他的消息，描述燃气具的",0));
                    }
                    childData.add(data);
                    break;
                case 1:
                    List<ServicesItems> data1=new ArrayList<>();
                    for (int j = 0; j <7 ; j++) {
                        data1.add(new ServicesItems("004"+(j+4),"计量表"+j,"25","我是其他的消息，描述计量表的",0));
                    }
                    childData.add(data1);
                    break;
                case 2:
                    List<ServicesItems> data3=new ArrayList<>();
                    for (int j = 0; j <7 ; j++) {
                        data3.add(new ServicesItems("008"+(j+4),"居民用气维修"+j,"25","我是其他的消息，描述居民用气维修的",0));
                    }
                    childData.add(data3);
                    break;
                case 3:
                    List<ServicesItems> data4=new ArrayList<>();
                    for (int j = 0; j <7 ; j++) {
                        data4.add(new ServicesItems("008"+(j+4),"居民用气户内改造"+j,"25","我是其他的消息，描述居民用气户内改造的",0));
                    }
                    childData.add(data4);
                    break;
                case 4:
                    List<ServicesItems> data5=new ArrayList<>();
                    for (int j = 0; j <7 ; j++) {
                        data5.add(new ServicesItems("008"+(j+4),"安全装置"+j,"25","我是其他的消息，描述安全装置的",0));
                    }
                    childData.add(data5);
                    break;
                case 5:
                    List<ServicesItems> data6=new ArrayList<>();
                    for (int j = 0; j <7 ; j++) {
                        data6.add(new ServicesItems("008"+(j+4),"其他"+j,"25","我是其他的消息，描述其他的",0));
                    }
                    childData.add(data6);
                    break;
            }
        }
        return childData;
    }
}
