package com.lzjs.uappoint.bean;

import java.io.Serializable;

/**
 * 装备Entity
 * Created by wangdq on 2016/1/15.
 */
public class Product implements Serializable {

    /**
     * createTime : 创建时间
     * productPic : noPic
     * productNum : 56
     * manufactorId : 402881a551d7e58e0151d7e84d7f0002
     * merchantName : 厂家
     * merchantId : 402881a551d7e58e0151d7e84d7f0002
     * productPrice : 价格
     * productName : 名称
     * productId : Id
     */

    private String createTime;
    private String productPic;
    private String productNum;
    private String manufactorId;
    private String merchantName;
    private String merchantId;
    private String productPrice;
    private String productName;
    private String productId;
    private String productZkPrice;
    private int num;

    private String productIntr;

    private String merchantAdd;

    private String merchantMobile;

    public String getMerchantAdd() {
        return merchantAdd;
    }

    public void setMerchantAdd(String merchantAdd) {
        this.merchantAdd = merchantAdd;
    }

    public String getMerchantMobile() {
        return merchantMobile;
    }

    public void setMerchantMobile(String merchantMobile) {
        this.merchantMobile = merchantMobile;
    }

    public void setProductIntr(String productIntr) {
        this.productIntr = productIntr;
    }

    public String getProductIntr() {

        return productIntr;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public void setManufactorId(String manufactorId) {
        this.manufactorId = manufactorId;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getProductPic() {
        return productPic;
    }

    public String getProductNum() {
        return productNum;
    }

    public String getManufactorId() {
        return manufactorId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductId() {
        return productId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getProductZkPrice() {
        return productZkPrice;
    }

    public void setProductZkPrice(String productZkPrice) {
        this.productZkPrice = productZkPrice;
    }
}
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
////        dest.writeString(productName);
////        dest.writeInt(num);
////        dest.writeString(productId);
////        dest.writeString(productPrice);
//    }
//
//    public Product() {
//
//    }
//
//    protected Product(Parcel in) {
//         num = in.readInt();
//         createTime = in.readString();
//         productPic = in.readString();
//         productNum = in.readString();
//         manufactorId = in.readString();
//         merchantName = in.readString();
//         merchantId = in.readString();
//         productPrice= in.readString();
//         productName= in.readString();
//         productId= in.readString();
//         productIntr= in.readString();
//         merchantAdd= in.readString();
//         merchantMobile= in.readString();
//    }
//
//    public static final Creator<Product> CREATOR = new Creator<Product>() {
//        @Override
//        public Product createFromParcel(Parcel in) {
//            return new Product(in);
//        }
//
//        @Override
//        public Product[] newArray(int size) {
//            return new Product[size];
//        }
//    };
//
//
//
//}
