package com.lzjs.uappoint.bean;

import java.io.Serializable;

/**
 * Created by lzjs on 15/12/23.
 */

public class Meeting implements Serializable {

    private String id;
    private String tital;//标题
    private String content; //内容
    private String type;  //类型  1：会议  2：通知
    private String url;   //url
    private String imgUrl;//网络图片资源
    private int imgPath=-1;//本地图片资源
    private String date; //日期
    private boolean read;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTital() {
        return tital;
    }

    public void setTital(String tital) {
        this.tital = tital;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
