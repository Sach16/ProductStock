package com.skpissay.productstock.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by S.K. Pissay on 16/05/17.
 */
public class Product {

    @SerializedName("tbl_product_id")
    @Expose
    private String tblProductId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("product name")
    @Expose
    private String productName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("tbl_stock_id")
    @Expose
    private String tblStockId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("stock")
    @Expose
    private String stock;

    public String getTblProductId() {
        return tblProductId;
    }

    public void setTblProductId(String tblProductId) {
        this.tblProductId = tblProductId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTblStockId() {
        return tblStockId;
    }

    public void setTblStockId(String tblStockId) {
        this.tblStockId = tblStockId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

}