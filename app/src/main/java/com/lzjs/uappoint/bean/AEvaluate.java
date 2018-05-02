package com.lzjs.uappoint.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 评价信息实体
 * Created by shalei on 2016/2/1.
 */
public class AEvaluate implements Serializable {

    /** 评价主键 */
    private String evaluateId;
    /** 用户Id */
    private String regiUserName;
    private String regiUserId;
    /** 商家Id */
    private String merchantName;
    private String merchantId;
    /** 类型  */
    private String typeId;
    /** 栏目Id（装备类型） */
    private String bsMenuName;
    private String menuId;
    /** 装备Id/场地Id*/
    private String venueOrProductName;
    private String equipId;
    /** 评价内容 */
    private String evaluateContent;
    /** 图片Id */
    private String evaluatePicId;
    /** 创建时间 */
    private String createTime;
    /** 更新时间 */
    private String updateTime;
    /** 有效状态 */
    private String status;
    /**评论图片*/
    private String regiUserPic;
    /**商家回复*/
    private List<MerchaneReply> replys;


    public String getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(String evaluateId) {
        this.evaluateId = evaluateId;
    }

    public String getRegiUserName() {
        return regiUserName;
    }

    public void setRegiUserName(String regiUserName) {
        this.regiUserName = regiUserName;
    }

    public String getRegiUserId() {
        return regiUserId;
    }

    public void setRegiUserId(String regiUserId) {
        this.regiUserId = regiUserId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getBsMenuName() {
        return bsMenuName;
    }

    public void setBsMenuName(String bsMenuName) {
        this.bsMenuName = bsMenuName;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getVenueOrProductName() {
        return venueOrProductName;
    }

    public void setVenueOrProductName(String venueOrProductName) {
        this.venueOrProductName = venueOrProductName;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }

    public String getEvaluatePicId() {
        return evaluatePicId;
    }

    public void setEvaluatePicId(String evaluatePicId) {
        this.evaluatePicId = evaluatePicId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegiUserPic() {
        return regiUserPic;
    }

    public void setRegiUserPic(String regiUserPic) {
        this.regiUserPic = regiUserPic;
    }

    public List<MerchaneReply> getReplys() {
        return replys;
    }

    public void setReplys(List<MerchaneReply> replys) {
        this.replys = replys;
    }
}
