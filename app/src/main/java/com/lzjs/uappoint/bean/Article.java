package com.lzjs.uappoint.bean;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

import lzjs.com.picplayer.bean.ADInfo;

/**
 * Created by lzjs on 15/12/23.
 */


public class Article implements Serializable {

    private String id;
    private String sceneId;
    private String tital;//标题
    private String imgUrl;//网络图片资源
    private int imgPath=-1;//本地图片资源
    private ImageView mImageView;
    private Bitmap bitmap;
    private String sceneTitle;
    private String infoPic;
    private String replyCount;
    private String voteCount;
    private String viewCount;
    private String createData;
    private List<String> images;
    private String author;

    private String type;
    private String url;
    private String class1;
    private String class2;
    private String class3;



    public ImageView getmImageView() {
        return mImageView;
    }
    public void setmImageView(ImageView mImageView) {
        this.mImageView = mImageView;
    }
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
    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneTitle() {
        return sceneTitle;
    }

    public void setSceneTitle(String sceneTitle) {
        this.sceneTitle = sceneTitle;
    }

    public String getInfoPic() {
        return infoPic;
    }

    public void setInfoPic(String infoPic) {
        this.infoPic = infoPic;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getCreateData() {
        return createData;
    }

    public void setCreateData(String createData) {
        this.createData = createData;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
    }

    public String getClass3() {
        return class3;
    }

    public void setClass3(String class3) {
        this.class3 = class3;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
