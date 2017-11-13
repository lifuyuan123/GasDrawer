package com.sctjsj.gasdrawer.entity.javaBean;

import java.io.Serializable;

/**
 * Created by liuha on 2017/5/25.
 */

public class WorkerBean implements Serializable{

    private int type;
    private int id;
    private String nickname;
    private String phone;
    private String sign;
    private String username;
    private String smallUrl;



    public WorkerBean(int type, int id, String nickname, String phone, String sign, String username) {
        this.type = type;
        this.id = id;
        this.nickname = nickname;
        this.phone = phone;
        this.sign = sign;
        this.username = username;
    }

    public WorkerBean() {
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "WorkerBean{" +
                "type=" + type +
                ", id=" + id +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", sign='" + sign + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
