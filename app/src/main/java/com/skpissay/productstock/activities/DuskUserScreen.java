package com.skpissay.productstock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.skpissay.productstock.R;
import com.skpissay.productstock.adapters.PagerAdapterForDusk;
import com.skpissay.productstock.baseclasses.DuskBaseActivity;
import com.skpissay.productstock.macros.DuskMacros;
import com.skpissay.productstock.models.Product;
import com.skpissay.productstock.models.ProductTable;
import com.skpissay.productstock.network.Constants;
import com.skpissay.productstock.network.RequestManager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by S.K. Pissay on 27/10/16.
 */

public class DuskUserScreen extends DuskBaseActivity {

    @Nullable
    @BindView(R.id.TAB_LAYOUT)
    TabLayout m_cTabLayout;

    @Nullable
    @BindView(R.id.PAGER)
    ViewPager m_cPager;

    private PagerAdapterForDusk m_cPagerAdapter;

    private int count;

    @Override
    protected void onCreate(Bundle pSavedInstance) {
        super.onCreate(pSavedInstance);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        init();
    }

    private void init() {
        m_cTabLayout.addTab(m_cTabLayout.newTab().setText("Category 1"));
        m_cTabLayout.addTab(m_cTabLayout.newTab().setText("Category 2"));
        m_cTabLayout.addTab(m_cTabLayout.newTab().setText("Category 3"));
//        m_cTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        m_cPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(m_cTabLayout));
        m_cPagerAdapter = new PagerAdapterForDusk(getSupportFragmentManager(),
                m_cObjFragmentBase,
                m_cTabLayout.getTabCount(),
                "");
        m_cPager.setOffscreenPageLimit(2);
        m_cPager.setAdapter(m_cPagerAdapter);

        m_cTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                m_cPager.setCurrentItem(tab.getPosition());
//                swipeView.setEnabled(false);
                switch (tab.getPosition()) {
                    case 0:
                        // NOTHING TO DO HERE
                        break;
                    case 1:
//                        setTitle("Lead Name", false, false, true, false);
//                        swipeView.setEnabled(true);
//                        displayProgressBar(-1, "Loading Packages,..");
//                        m_cObjTransportMgr.getPackages("", EURemediesSpecialityScreen.this);
                        break;
                    case 2:
                        // NOTHING TO DO HERE
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Nothing to do here
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Nothing to do here
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void handleUIMessage(Message pObjMessage) {
        Intent lObjIntent;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod, Object refObj) {
        if (apiMethod.contains(Constants.OFFLINE_META)) {
            Object[] lObjects = (Object[]) refObj;
            try {
                byte[] lByte = (byte[]) response;
                long lenghtOfFile = lByte.length;

                //coverting reponse to input stream
                InputStream input = new ByteArrayInputStream(lByte);
                File file = new File((String) lObjects[1]);
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateProduct((Product) lObjects[0], (String) lObjects[1]);
        }else {
            super.onAPIResponse(response, apiMethod, refObj);
            hideDialog();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod, Object refObj) {
        switch (apiMethod) {
            default:
                if (apiMethod.contains(Constants.OFFLINE_META)) {
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

    public void downloadAndUpdateProduct(Product pProduct) {
        RequestManager.getInstance(this).placeStreamRequest(DuskMacros.checkAndUpdateUrl(pProduct.getImage()),
                Constants.OFFLINE_META,
                Product.class,
                this,
                new Object[]{pProduct,
                        DuskMacros.getOfflineImageFilePath(this) + "/" + DuskMacros.getGUID() + ".jpg"},
                null, null, false);
    }

    private void updateProduct(Product pProduct, String pUrl){
        ProductTable lProductTable = new ProductTable(pProduct.getCategoryId(), pUrl, pProduct.getTblProductId());
        lProductTable.save();
    }

}
