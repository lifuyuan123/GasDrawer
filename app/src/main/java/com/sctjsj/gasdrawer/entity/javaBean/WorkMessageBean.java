package com.sctjsj.gasdrawer.entity.javaBean;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.gasdrawer.entity.ChildBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuha on 2017/5/22.
 */

public class WorkMessageBean implements Serializable {
    private int id;
    private int ordertype; //1分户安装 2户内改造 3燃气具维修 4安检整改
    private String address;//用户地址
    private String departName;//作业部门
    private String orderNo;//工作单号
    private String tel;//用户电话
    private String CAENNumber;//CAE号
    private String clientName;//客户名称
    private String insertTime;//派单时间
    private int orderStatus;//工单状态
    private int totalprice;//订单金额
    private int isDelete;
    private String payTime;
    private String houseId;
    private String houseName;
    private String acceptedNumber;
    private String repairReason;
    private String planner;//规划人
    private String accNumber;
    private String ceaNo;
    private List<ServerChildBean> serverChildBeen;
    private List<ChildBean> material;
    private List<ImageBean> picAddress;
    private String applianceName;

    public String getApplianceName() {
        return applianceName;
    }

    public void setApplianceName(String applianceName) {
        this.applianceName = applianceName;
    }

    public List<ChildBean> getMaterial() {
        return material;
    }

    public void setMaterial(List<ChildBean> material) {
        this.material = material;
    }

    public List<ImageBean> getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(List<ImageBean> picAddress) {
        this.picAddress = picAddress;
    }

    public String getCeaNo() {
        return ceaNo;
    }

    public void setCeaNo(String ceaNo) {
        this.ceaNo = ceaNo;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public List<ServerChildBean> getServerChildBeen() {
        return serverChildBeen;
    }

    public void setServerChildBeen(List<ServerChildBean> serverChildBeen) {
        this.serverChildBeen = serverChildBeen;
    }

    public String getPlanner() {
        return planner;
    }

    public void setPlanner(String planner) {
        this.planner = planner;
    }

    public String getRepairReason() {
        return repairReason;
    }

    public void setRepairReason(String repairReason) {
        this.repairReason = repairReason;
    }

    public String getAcceptedNumber() {
        return acceptedNumber;
    }

    public void setAcceptedNumber(String acceptedNumber) {
        this.acceptedNumber = acceptedNumber;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    private String installTime;


    public WorkMessageBean() {
    }

    public WorkMessageBean(int id, int ordertype, String address, String departName, String orderNo, String tel, String CAENNumber, String clientName, String insertTime, int orderStatus, int totalprice) {
        this.id = id;
        this.ordertype = ordertype;
        this.address = address;
        this.departName = departName;
        this.orderNo = orderNo;
        this.tel = tel;
        this.CAENNumber = CAENNumber;
        this.clientName = clientName;
        this.insertTime = insertTime;
        this.orderStatus = orderStatus;
        this.totalprice = totalprice;
    }


    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(int ordertype) {
        this.ordertype = ordertype;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCAENNumber() {
        return CAENNumber;
    }

    public void setCAENNumber(String CAENNumber) {
        this.CAENNumber = CAENNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    @Override
    public String toString() {
        return "WorkMessageBean{" +
                "id=" + id +
                ", ordertype=" + ordertype +
                ", address='" + address + '\'' +
                ", departName='" + departName + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", tel='" + tel + '\'' +
                ", CAENNumber='" + CAENNumber + '\'' +
                ", clientName='" + clientName + '\'' +
                ", insertTime='" + insertTime + '\'' +
                ", orderStatus=" + orderStatus +
                ", totalprice=" + totalprice +
                ", isDelete=" + isDelete +
                ", payTime='" + payTime + '\'' +
                ", houseId='" + houseId + '\'' +
                ", houseName='" + houseName + '\'' +
                ", acceptedNumber='" + acceptedNumber + '\'' +
                ", repairReason='" + repairReason + '\'' +
                ", planner='" + planner + '\'' +
                ", accNumber='" + accNumber + '\'' +
                ", ceaNo='" + ceaNo + '\'' +
                ", serverChildBeen=" + serverChildBeen +
                ", material=" + material +
                ", picAddress=" + picAddress +
                ", applianceName='" + applianceName + '\'' +
                ", installTime='" + installTime + '\'' +
                '}';
    }
}
