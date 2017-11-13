package com.sctjsj.gasdrawer.entity;

import java.io.Serializable;

/**
 * Created by liuha on 2017/4/27.
 */

public class ServicesItems implements Serializable{
    private String serNumber;
    private String serName;
    private String serPrice;
    private String serMsg;
    private int  mPrice;
    private int  type;

    public String getEditext() {
        return editext;
    }

    public void setEditext(String editext) {
        this.editext = editext;
    }

    private String editext;

    public ServicesItems() {
    }

    public ServicesItems(String serNumber, String serName, String serPrice, String serMsg, int mPrice) {
        this.serNumber = serNumber;
        this.serName = serName;
        this.serPrice = serPrice;
        this.serMsg = serMsg;
        this.mPrice = mPrice;
    }

    public int getmPrice() {
        return mPrice;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSerNumber() {
        return serNumber;
    }

    public void setSerNumber(String serNumber) {
        this.serNumber = serNumber;
    }

    public String getSerName() {
        return serName;
    }

    public void setSerName(String serName) {
        this.serName = serName;
    }

    public String getSerPrice() {
        return serPrice;
    }

    public void setSerPrice(String serPrice) {
        this.serPrice = serPrice;
    }

    public String getSerMsg() {
        return serMsg;
    }

    public void setSerMsg(String serMsg) {
        this.serMsg = serMsg;
    }

    @Override
    public String toString() {
        return "ServicesItems{" +
                "serNumber='" + serNumber + '\'' +
                ", serName='" + serName + '\'' +
                ", serPrice='" + serPrice + '\'' +
                ", serMsg='" + serMsg + '\'' +
                ", mPrice=" + mPrice +
                ", type=" + type +
                '}';
    }
}
