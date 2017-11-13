package com.sctjsj.gasdrawer.entity.javaBean;

import java.io.Serializable;

/**
 * Created by liuha on 2017/5/23.
 */

public class MessageBean implements Serializable {
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private String content;
    private int id;
    private String publishTime;
    private String title;

    public MessageBean(String content, int id, String publishTime, String title) {
        this.content = content;
        this.id = id;
        this.publishTime = publishTime;
        this.title = title;
    }

    public MessageBean() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "num=" + num +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", publishTime='" + publishTime + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
