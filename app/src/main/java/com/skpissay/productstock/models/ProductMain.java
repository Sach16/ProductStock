package com.skpissay.productstock.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by S.K. Pissay on 16/05/17.
 */
public class ProductMain {

    @SerializedName("products")
    @Expose
    private Product product;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
