package com.sctjsj.gasdrawer.entity.javaBean;

import java.io.Serializable;

/**
 * Created by liuha on 2017/5/5.
 */

public class RepairGasBean implements Serializable{
    private String no;//型号
    private String brand;//品牌
    private String serNumber;//燃具序列
    private String installTime;//安装日期
    private Integer isRepair;//故障维修
    private Integer isChange;//更换配件
    private Integer isMaintain;//保养
    private Integer repairWay;//维修方式

    public RepairGasBean(String no, String brand, String serNumber, String installTime, Integer isRepair, Integer isChange, Integer isMaintain, Integer repairWay) {
        this.no = no;
        this.brand = brand;
        this.serNumber = serNumber;
        this.installTime = installTime;
        this.isRepair = isRepair;
        this.isChange = isChange;
        this.isMaintain = isMaintain;
        this.repairWay = repairWay;
    }

    public RepairGasBean() {
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSerNumber() {
        return serNumber;
    }

    public void setSerNumber(String serNumber) {
        this.serNumber = serNumber;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public Integer getIsRepair() {
        return isRepair;
    }

    public void setIsRepair(Integer isRepair) {
        this.isRepair = isRepair;
    }

    public Integer getIsChange() {
        return isChange;
    }

    public void setIsChange(Integer isChange) {
        this.isChange = isChange;
    }

    public Integer getIsMaintain() {
        return isMaintain;
    }

    public void setIsMaintain(Integer isMaintain) {
        this.isMaintain = isMaintain;
    }

    public Integer getRepairWay() {
        return repairWay;
    }

    public void setRepairWay(Integer repairWay) {
        this.repairWay = repairWay;
    }

    @Override
    public String toString() {
        return "RepairGasBean{" +
                "no='" + no + '\'' +
                ", brand='" + brand + '\'' +
                ", serNumber='" + serNumber + '\'' +
                ", installTime='" + installTime + '\'' +
                ", isRepair='" + isRepair + '\'' +
                ", isChange='" + isChange + '\'' +
                ", isMaintain='" + isMaintain + '\'' +
                ", repairWay='" + repairWay + '\'' +
                '}';
    }
}
