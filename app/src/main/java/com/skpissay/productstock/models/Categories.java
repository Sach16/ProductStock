package com.skpissay.productstock.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by S.K. Pissay on 16/05/17.
 */
public class Categories {

    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
