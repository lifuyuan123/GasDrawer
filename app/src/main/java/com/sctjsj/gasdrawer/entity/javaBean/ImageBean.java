package com.sctjsj.gasdrawer.entity.javaBean;

import java.io.Serializable;

/**
 * Created by lifuy on 2017/6/16.
 */

public class ImageBean implements Serializable{
    private int type;
    private String title;
    private String url;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "type=" + type +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
