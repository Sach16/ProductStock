package com.skpissay.productstock.activities;

import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.skpissay.productstock.R;
import com.skpissay.productstock.adapters.CustomRecyclerAdapterForDusk;
import com.skpissay.productstock.baseclasses.DuskBaseActivity;
import com.skpissay.productstock.macros.DuskMacros;
import com.skpissay.productstock.models.Categories;
import com.skpissay.productstock.models.Product;
import com.skpissay.productstock.models.ProductMain;
import com.skpissay.productstock.models.ProductTable;
import com.skpissay.productstock.network.Constants;
import com.skpissay.productstock.network.RequestManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by S.K. Pissay on 17/05/17.
 */
public class DuskProductScreen extends DuskBaseActivity{

    @Nullable
    @BindView(R.id.image_view)
    ImageView image;

    @Nullable
    @BindView(R.id.title_text)
    TextView title;

    @Nullable
    @BindView(R.id.desc_text)
    TextView desc;

    @Nullable
    @BindView(R.id.info)
    TextView info;

    @Nullable
    @BindView(R.id.add_cart)
    Button cartBtn;

    private Product mProduct;

    @Override
    protected void onCreate(Bundle pSavedInstance) {
        super.onCreate(pSavedInstance);
        setContentView(R.layout.product_screen);
        ButterKnife.bind(this);

        mProduct = (new Gson()).fromJson(getIntent().getStringExtra(DuskMacros.OBJ_PRODUCT), Product.class);

        getSupportActionBar().setTitle(mProduct.getProductName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init() {
        displayProgressBar(-1, "");
        HashMap<String, String> lParams = new HashMap<>();
        lParams.put(Constants.TOKEN, Constants.VIRTUALDUSK);
        lParams.put(Constants.PRODUCT_ID, mProduct.getTblProductId());
        RequestManager.getInstance(this).placeRequest(Constants.PRODUCT_DETAILS, ProductMain.class, this, null, lParams, null, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void handleUIMessage(Message pObjMessage) {

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod, Object refObj) {
        switch (apiMethod){
            case Constants.PRODUCT_DETAILS:
                ProductMain lProductMain = (ProductMain) response;
                if (lProductMain != null) {
                    try {
                        Picasso.with(this)
                                .load(DuskMacros.checkAndUpdateUrl(lProductMain.getProduct().getImage()))
                                .error(R.drawable.profile_placeholder)
                                .placeholder(R.drawable.profile_placeholder)
                                .fit()
                                .into(image);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    title.setText(lProductMain.getProduct().getProductName());

                    desc.setText(lProductMain.getProduct().getDescription());

                    switch (Integer.parseInt(lProductMain.getProduct().getStock())){
                        case 0:
                            cartBtn.setVisibility(View.GONE);
                            info.setText(getResources().getString(R.string.out_of_stock));
                            break;
                        case 1:
                        case 2:
                            cartBtn.setVisibility(View.VISIBLE);
                            info.setText(getResources().getString(R.string.only_2_in_stock));
                            break;
                        default:
                            info.setText(getResources().getString(R.string.in_stock));
                            cartBtn.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                hideDialog();
                break;
            default:
                super.onAPIResponse(response, apiMethod, refObj);
                hideDialog();
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod, Object refObj) {
        switch (apiMethod) {
            default:
                if (apiMethod.contains(Constants.PRODUCT_DETAILS)) {
                    hideDialog();
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(this, "Please check Network connection", Toast.LENGTH_SHORT).show();
                    } else {
                        String lMsg = new String(error.networkResponse.data);
                        showErrorMsg(lMsg);
                    }
                } else {
                    super.onErrorResponse(error, apiMethod, refObj);
                    hideDialog();
                }
                break;
        }
    }
}
