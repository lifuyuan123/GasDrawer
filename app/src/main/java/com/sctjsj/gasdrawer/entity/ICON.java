package com.sctjsj.gasdrawer.entity;

/**
 * Created by mayikang on 17/4/13.
 */

public class ICON {
    private int id;

    private int resId;

    private String name;

    public ICON(int id, String name, int resId) {
        this.id = id;
        this.name = name;
        this.resId = resId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
