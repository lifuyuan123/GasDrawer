package com.sctjsj.gasdrawer.entity.javaBean;

import java.io.Serializable;

/**
 * Created by liuha on 2017/5/24.
 *客户信息的javabean
 */

public class CustomerBean implements Serializable {
    private String address;
    private String CAENo;
    private String clientName;//用户名称
    private String departName;//部门名称
    private int id;
    private String installTime;//安装时间
    private String insertTime;//插入时间（报修时间）
    private String orderNo;//工作单号
    private  int orderStatus;//1分户安装 2户内改造 3燃气具维修 4安检整改
    private String tel;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public CustomerBean(String address, String CAENo, String clientName, String departName, int id, String installTime, String insertTime, String orderNo, int orderStatus) {
        this.address = address;
        this.CAENo = CAENo;
        this.clientName = clientName;
        this.departName = departName;
        this.id = id;
        this.installTime = installTime;
        this.insertTime = insertTime;
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
    }

    public CustomerBean() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCAENo() {
        return CAENo;
    }

    public void setCAENo(String CAENo) {
        this.CAENo = CAENo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "CustomerBean{" +
                "address='" + address + '\'' +
                ", CAENo='" + CAENo + '\'' +
                ", clientName='" + clientName + '\'' +
                ", departName='" + departName + '\'' +
                ", id=" + id +
                ", installTime='" + installTime + '\'' +
                ", insertTime='" + insertTime + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
