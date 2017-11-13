package com.sctjsj.gasdrawer.entity;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.io.Serializable;

/**
 * Created by lifuy on 2017/5/22.
 */


public class GroupBean implements Serializable {
    String className,time;
    int id,type;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "GroupBean{" +
                "className='" + className + '\'' +
                ", time='" + time + '\'' +
                ", id=" + id +
                ", type=" + type +
                '}';
    }
}
