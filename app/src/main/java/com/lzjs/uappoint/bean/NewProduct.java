package com.lzjs.uappoint.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/11.
 */
public class NewProduct implements Parcelable{
    private String productPrice;
    private String productName;
    private String productId;
    private int num;
    private String productPic;

    public NewProduct(Parcel in) {
        productPrice = in.readString();
        productName = in.readString();
        productId = in.readString();
        num = in.readInt();
        productPic=in.readString();
    }

    public static final Creator<NewProduct> CREATOR = new Creator<NewProduct>() {
        @Override
        public NewProduct createFromParcel(Parcel in) {
            NewProduct mProduct=new NewProduct();
            mProduct.num=in.readInt();
            mProduct.productId= in.readString();
            mProduct.productName= in.readString();
            mProduct.productPrice= in.readString();
            mProduct.productPic=in.readString();

            return mProduct;
        }

        @Override
        public NewProduct[] newArray(int size) {
            return new NewProduct[size];
        }
    };

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num);
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(productPrice);
        dest.writeString(productPic);
    }
   public  NewProduct(){

    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }
}
