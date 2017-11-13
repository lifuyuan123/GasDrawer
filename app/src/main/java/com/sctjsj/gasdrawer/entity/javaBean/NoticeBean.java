package com.sctjsj.gasdrawer.entity.javaBean;

import java.io.Serializable;

/**
 * Created by liuha on 2017/5/23.
 * 首页公告通知的javabean
 */

public class NoticeBean implements Serializable {
    private String content;
    private int id;
    private String publishTime;
    private String title;

    public NoticeBean(String content, int id, String publishTime, String title) {
        this.content = content;
        this.id = id;
        this.publishTime = publishTime;
        this.title = title;
    }

    public NoticeBean() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
