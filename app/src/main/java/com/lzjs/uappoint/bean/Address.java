package com.lzjs.uappoint.bean;

import java.io.Serializable;

/**
 * 收货地址
 * Created by wangdianqiang on 2016/3/7.
 */
public class Address implements Serializable {


    /**
     * addressCityName : 上海市
     * addressCity : f26227ae62004a3f8a7f2b5646f2ec25
     * updateTime : 2015-12-24 10:24:32
     * addressCountyName : 闸北区
     * addressCounty : 1e4d512af520416983f4982c303643b3
     * postcode : 730020
     * addressPro : cd5bc2bcc5434199bd9ecaecc014dfff
     * addressId : 123
     * addressProName : 上海市
     * phoneNumber : 13519317257
     * isDefault : 001
     * detailedAddress : 新民国际1号
     * realname : 杨瑞
     * mobileNumber : 13519317257
     */

    private String addressCityName;
    private String addressCity;
    private String updateTime;
    private String addressCountyName;
    private String addressCounty;
    private String postcode;
    private String addressPro;
    private String addressId;
    private String addressProName;
    private String phoneNumber;
    private String isDefault;
    private String detailedAddress;
    private String realname;
    private String mobileNumber;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAddressCityName(String addressCityName) {
        this.addressCityName = addressCityName;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setAddressCountyName(String addressCountyName) {
        this.addressCountyName = addressCountyName;
    }

    public void setAddressCounty(String addressCounty) {
        this.addressCounty = addressCounty;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setAddressPro(String addressPro) {
        this.addressPro = addressPro;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public void setAddressProName(String addressProName) {
        this.addressProName = addressProName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddressCityName() {
        return addressCityName;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getAddressCountyName() {
        return addressCountyName;
    }

    public String getAddressCounty() {
        return addressCounty;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getAddressPro() {
        return addressPro;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getAddressProName() {
        return addressProName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public String getRealname() {
        return realname;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
