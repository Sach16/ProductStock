package com.skpissay.productstock.interfaces;

import android.view.View;

import com.skpissay.productstock.models.Product;


/**
 * Created by S.K. Pissay on 30/12/16.
 */

public interface DuskListener {
    public void onInfoClick(int pPostion, Product pProduct, View pView);
    public void downloadImage(int pPostion, Product pProduct);
}
