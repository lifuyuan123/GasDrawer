package com.sctjsj.gasdrawer.entity.javaBean;

import java.io.Serializable;

/**
 * Created by lifuy on 2017/5/25.
 */

public class ServerChildBean implements Serializable {
    //id  价格
    int id;
    int price;
    int type;
    int totalPrice;
    int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }



    //名字  评论  单位数
    String proName,remarks,unit,editext;

    public ServerChildBean(int id, int price, String proName, String remarks, String unit) {
        this.id = id;
        this.price = price;
        this.proName = proName;
        this.remarks = remarks;
        this.unit = unit;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEditext() {
        return editext;
    }

    public void setEditext(String editext) {
        this.editext = editext;
    }

    public ServerChildBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "ServerChildBean{" +
                "id=" + id +
                ", price=" + price +
                ", proName='" + proName + '\'' +
                ", remarks='" + remarks + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
