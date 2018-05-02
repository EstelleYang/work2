package com.lzjs.uappoint.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 订单
 * Created by wangdq on 2016/1/26.
 */
public class Order implements Serializable {


    /**
     * orderCode : 123
     * orderDate : 2015-12-25 12:00:00
     * orderStatus : 00A
     * regiUserId : 123
     * merchantName : 123
     * merchantId : 123
     * orderPrice : 123
     * regiUserName : 123
     * orderId : 1233
     * updateTime : 2015-12-25 12:00:00
     */

    private String orderCode;
    private String orderDate;
    private String orderStatus;
    private String regiUserId;
    private String merchantName;
    private String merchantId;
    private String orderPrice;
    private String regiUserName;
    private String orderId;
    private String updateTime;
    private String orderTime;
    private String venueName;
    private String orderState;
    private String productName;
    private String vegetablePrice;//单价
    private String vegetableNum;
    private String friendAdd;
    private String vegetableTotal;
    private String remark;
    private String eatMethod;
    private String productPic;
    private String venueId;
    private String menuIds;
    private String productId;
    private ArrayList<DetailOrder> orderDetails;



    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setRegiUserId(String regiUserId) {
        this.regiUserId = regiUserId;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setRegiUserName(String regiUserName) {
        this.regiUserName = regiUserName;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getRegiUserId() {
        return regiUserId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public String getRegiUserName() {
        return regiUserName;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVegetablePrice() {
        return vegetablePrice;
    }

    public void setVegetablePrice(String vegetablePrice) {
        this.vegetablePrice = vegetablePrice;
    }

    public String getVegetableNum() {
        return vegetableNum;
    }

    public void setVegetableNum(String vegetableNum) {
        this.vegetableNum = vegetableNum;
    }

    public String getFriendAdd() {
        return friendAdd;
    }

    public void setFriendAdd(String friendAdd) {
        this.friendAdd = friendAdd;
    }

    public String getVegetableTotal() {
        return vegetableTotal;
    }

    public void setVegetableTotal(String vegetableTotal) {
        this.vegetableTotal = vegetableTotal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEatMethod() {
        return eatMethod;
    }

    public void setEatMethod(String eatMethod) {
        this.eatMethod = eatMethod;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }


    public String getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ArrayList<DetailOrder> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<DetailOrder> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
