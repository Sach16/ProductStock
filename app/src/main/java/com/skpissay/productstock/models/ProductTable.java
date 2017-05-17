package com.skpissay.productstock.models;

import com.google.android.gms.nearby.messages.Strategy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by S.K. Pissay on 17/05/17.
 */
public class ProductTable extends SugarRecord{

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("product_id")
    @Expose
    private String productId;

    public ProductTable(){}

    public ProductTable(String categoryId, String image, String productId){
        this.categoryId = categoryId;
        this.image = image;
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
