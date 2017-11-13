package com.sctjsj.gasdrawer.entity;

import java.io.Serializable;

/**
 * Created by lifuy on 2017/5/23.
 */

public class ChildBean implements Serializable {
//purchasePrice 购买价格 ,sellingPrice 销售价格，remarks 评论，specInfo 规格信息，unit 单位数
    int id;
    int purchasePrice;
    int sellingPrice;
    int type;
    int all;
    String materialNo;
    String materialName;
    String model;
    String remarks;
    String specInfo;
    String unit;
    String editext;
    String count;
    int freeCount;
    int receivableCount;

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public int getFreeCount() {
        return freeCount;
    }

    public void setFreeCount(int freeCount) {
        this.freeCount = freeCount;
    }

    public int getReceivableCount() {
        return receivableCount;
    }

    public void setReceivableCount(int receivableCount) {
        this.receivableCount = receivableCount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ChildBean(int id, int purchasePrice, int sellingPrice, int type, String materialName, String model, String remarks, String specInfo, String unit, String editext) {
        this.id = id;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.type = type;
        this.materialName = materialName;
        this.model = model;
        this.remarks = remarks;
        this.specInfo = specInfo;
        this.unit = unit;
        this.editext = editext;
    }

    public ChildBean() {
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
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



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSpecInfo() {
        return specInfo;
    }

    public void setSpecInfo(String specInfo) {
        this.specInfo = specInfo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "ChildBean{" +
                "id=" + id +
                ", purchasePrice=" + purchasePrice +
                ", sellingPrice=" + sellingPrice +
                ", type=" + type +
                ", all=" + all +
                ", materialNo=" + materialNo +
                ", materialName='" + materialName + '\'' +
                ", model='" + model + '\'' +
                ", remarks='" + remarks + '\'' +
                ", specInfo='" + specInfo + '\'' +
                ", unit='" + unit + '\'' +
                ", editext='" + editext + '\'' +
                ", count='" + count + '\'' +
                ", freeCount=" + freeCount +
                ", receivableCount=" + receivableCount +
                '}';
    }
}
