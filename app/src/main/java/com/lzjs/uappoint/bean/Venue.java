package com.lzjs.uappoint.bean;

import java.io.Serializable;

/**
 * 场地实体
 * Created by ShaLei on 2016/1/27.
 */
public class Venue implements Serializable {

    private String merchantId;
    private String merchantName;
    private String venueId;
    private String venueName;
    private String venueIntr;
    private String openTime;
    private String venueType;
    private String venueNum;
    private String venuePic;
    private String venuePrice;
    private String venueTel;
    private String venueAddress;
    private String merchantPic;
    private String venueZkPrice;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueIntr() {
        return venueIntr;
    }

    public void setVenueIntr(String venueIntr) {
        this.venueIntr = venueIntr;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getVenueType() {
        return venueType;
    }

    public void setVenueType(String venueType) {
        this.venueType = venueType;
    }

    public String getVenueNum() {
        return venueNum;
    }

    public void setVenueNum(String venueNum) {
        this.venueNum = venueNum;
    }

    public String getVenuePic() {
        return venuePic;
    }

    public void setVenuePic(String venuePic) {
        this.venuePic = venuePic;
    }

    public String getVenuePrice() {
        return venuePrice;
    }

    public void setVenuePrice(String venuePrice) {
        this.venuePrice = venuePrice;
    }

    public String getVenueTel() {
        return venueTel;
    }

    public void setVenueTel(String venueTel) {
        this.venueTel = venueTel;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getMerchantPic() {
        return merchantPic;
    }

    public void setMerchantPic(String merchantPic) {
        this.merchantPic = merchantPic;
    }

    public String getVenueZkPrice() {
        return venueZkPrice;
    }

    public void setVenueZkPrice(String venueZkPrice) {
        this.venueZkPrice = venueZkPrice;
    }
}
