package com.sctjsj.gasdrawer.entity.javaBean;

import java.io.Serializable;

/**
 * Created by lifuy on 2017/6/5.
 */

public class Message implements Serializable{
    private String s;
    private int num;

    public Message(String s, int num) {
        this.s = s;
        this.num = num;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "s='" + s + '\'' +
                ", num=" + num +
                '}';
    }
}
