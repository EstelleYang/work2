package com.lzjs.uappoint.bean;

import java.io.Serializable;

/**
 * 订单详细信息
 * Created by wangdq on 2016/3/8.
 */
public class OrderDetail implements Serializable {


    /**
     * orderDetailId : 123
     * updateTime : 2015-12-25 12:00:00
     * vegetableNum : 1
     * merchantName : 商家名称01
     * merchantId : 123
     * vegetableTotal : 1
     * productName : 101
     * regiUserName : 用户01
     * orderId : 1111
     * vegetablePrice : 1
     * productId : 0
     * regiUserId : 123
     * productPic : http://
     */

    private String orderDetailId;
    private String updateTime;
    private String vegetableNum;
    private String merchantName;
    private String merchantId;
    private String vegetableTotal;
    private String productName;
    private String regiUserName;
    private String orderId;
    private String vegetablePrice;
    private String productId;
    private String regiUserId;
    private String productPic;

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setVegetableNum(String vegetableNum) {
        this.vegetableNum = vegetableNum;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setVegetableTotal(String vegetableTotal) {
        this.vegetableTotal = vegetableTotal;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setRegiUserName(String regiUserName) {
        this.regiUserName = regiUserName;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setVegetablePrice(String vegetablePrice) {
        this.vegetablePrice = vegetablePrice;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setRegiUserId(String regiUserId) {
        this.regiUserId = regiUserId;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getVegetableNum() {
        return vegetableNum;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getVegetableTotal() {
        return vegetableTotal;
    }

    public String getProductName() {
        return productName;
    }

    public String getRegiUserName() {
        return regiUserName;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getVegetablePrice() {
        return vegetablePrice;
    }

    public String getProductId() {
        return productId;
    }

    public String getRegiUserId() {
        return regiUserId;
    }

    public String getProductPic() {
        return productPic;
    }
}
