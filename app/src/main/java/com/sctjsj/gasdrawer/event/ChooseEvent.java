package com.sctjsj.gasdrawer.event;

/**
 * Created by mayikang on 17/4/13.
 */

public class ChooseEvent {
    private int resId;

    public ChooseEvent(int resId) {
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }
}
