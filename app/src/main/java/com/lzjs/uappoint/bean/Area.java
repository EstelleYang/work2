package com.lzjs.uappoint.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/11.
 */
public class Area  implements Serializable {


    /**
     * areaName : 北京市
     * areaId : e7805b8823b0454998e2437fcabd8bde
     * areaType : 00B
     * areaAlpha : 首字符
     */

    private String areaName;
    private String areaId;
    private String areaType;
    private String areaAlpha;

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public void setAreaAlpha(String areaAlpha) {
        this.areaAlpha = areaAlpha;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public String getAreaType() {
        return areaType;
    }

    public String getAreaAlpha() {
        return areaAlpha;
    }
}
