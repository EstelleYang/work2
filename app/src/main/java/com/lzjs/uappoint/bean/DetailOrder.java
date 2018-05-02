package com.lzjs.uappoint.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/11.
 */
public class DetailOrder implements Serializable {
    private String productPic;
    private String vegetableNum;
    private String vegetableTotal;
    private String productName;
    private String vegetablePrice;


    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getVegetableNum() {
        return vegetableNum;
    }

    public void setVegetableNum(String vegetableNum) {
        this.vegetableNum = vegetableNum;
    }

    public String getVegetableTotal() {
        return vegetableTotal;
    }

    public void setVegetableTotal(String vegetableTotal) {
        this.vegetableTotal = vegetableTotal;
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
}
