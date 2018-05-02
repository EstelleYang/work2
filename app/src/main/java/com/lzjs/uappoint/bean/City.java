package com.lzjs.uappoint.bean;

import java.io.Serializable;

/**
 * Created by wangdq on 2016/2/3.
 */
public class City implements Serializable {

    private String areaId;//区域主键
    private String areaName;//区域名称
    //private String sortKey;
    private String areaAlpha;//区域首字母
    private String areaType;//区域类型

    public String getAreaId() {
        return areaId;
    }

    @Override
    public String toString() {
        return "City{" +
                "areaId='" + areaId + '\'' +
                ", areaName='" + areaName + '\'' +
                ", areaAlpha='" + areaAlpha + '\'' +
                ", areaType='" + areaType + '\'' +
                '}';
    }
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaAlpha() {
        return areaAlpha;
    }

    public void setAreaAlpha(String areaAlpha) {
        this.areaAlpha = areaAlpha;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }
}

