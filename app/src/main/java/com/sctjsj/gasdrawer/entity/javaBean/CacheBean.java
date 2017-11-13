package com.sctjsj.gasdrawer.entity.javaBean;

import com.sctjsj.gasdrawer.entity.ChildBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by lifuy on 2017/6/8.
 */

public class CacheBean implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    //材料
    private List<ChildBean> childset;
    //服务
    private List<ServerChildBean> list1;
    //维修照片
    private List<String> picId;
    //其他维护事项
    private Map<String,String> datas;
//    故障与维修情况
    private Map<String, String> deta;
//    现场查看故障原因
    private Map<String,String> stringMap;
//    燃具维修
    private List<RepairGasBean> gasBeen;

    public List<ChildBean> getChildset() {
        return childset;
    }

    public void setChildset(List<ChildBean> childset) {
        this.childset = childset;
    }

    public List<ServerChildBean> getList1() {
        return list1;
    }

    public void setList1(List<ServerChildBean> list1) {
        this.list1 = list1;
    }

    public List<String> getPicId() {
        return picId;
    }

    public void setPicId(List<String> picId) {
        this.picId = picId;
    }

    public Map<String, String> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, String> datas) {
        this.datas = datas;
    }

    public Map<String, String> getDeta() {
        return deta;
    }

    public void setDeta(Map<String, String> deta) {
        this.deta = deta;
    }

    public Map<String, String> getStringMap() {
        return stringMap;
    }

    public void setStringMap(Map<String, String> stringMap) {
        this.stringMap = stringMap;
    }

    public List<RepairGasBean> getGasBeen() {
        return gasBeen;
    }

    public void setGasBeen(List<RepairGasBean> gasBeen) {
        this.gasBeen = gasBeen;
    }

    @Override
    public String toString() {
        return "CacheBean{" +
                "id=" + id +
                ", childset=" + childset +
                ", list1=" + list1 +
                ", picId=" + picId +
                ", datas=" + datas +
                ", deta=" + deta +
                ", stringMap=" + stringMap +
                ", gasBeen=" + gasBeen +
                '}';
    }
}
