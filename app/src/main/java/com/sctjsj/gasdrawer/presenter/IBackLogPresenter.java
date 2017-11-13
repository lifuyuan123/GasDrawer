package com.sctjsj.gasdrawer.presenter;

import com.sctjsj.gasdrawer.util.BluetoothService;

import java.util.List;
import java.util.Map;

/**
 * Created by liuha on 2017/4/25.
 */

public interface IBackLogPresenter {
    /**
     * 加载数据
     * @return  带数据的fragment
     */
    public Map<String,List<?>> initdata();
    /**
     * 初始化
     */
    public void init();
}
