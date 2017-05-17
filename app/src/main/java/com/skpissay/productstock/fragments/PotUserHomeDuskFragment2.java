package com.skpissay.productstock.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.google.gson.Gson;
import com.skpissay.productstock.R;
import com.android.volley.VolleyError;
import com.skpissay.productstock.activities.DuskProductScreen;
import com.skpissay.productstock.activities.DuskUserScreen;
import com.skpissay.productstock.adapters.CustomRecyclerAdapterForDusk;
import com.skpissay.productstock.adapters.CustomRecyclerAdapterForDusk2;
import com.skpissay.productstock.baseclasses.DuskFragmentBaseClass;
import com.skpissay.productstock.interfaces.DuskListener;
import com.skpissay.productstock.macros.DuskMacros;
import com.skpissay.productstock.models.Categories;
import com.skpissay.productstock.models.Product;
import com.skpissay.productstock.network.Constants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by S.K. Pissay on 17/05/17.
 */
public class PotUserHomeDuskFragment2 extends DuskFragmentBaseClass implements DuskListener {

    @Nullable
    @BindView(R.id.HOME_CLASS_MAIN_LAYOUT)
    RelativeLayout m_cRlMain;

    @Nullable
    @BindView(R.id.RECYC_HOME_CLASS_BOARDS)
    RecyclerView m_cRecycClasses;

    private int m_cPos;
    private String m_cKey;

    private String m_cGoOffline;

    private Dialog m_cObjDialog;

    private boolean m_cLoading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager m_cLayoutManager;
    CustomRecyclerAdapterForDusk2 m_cRecycClassesAdapt;
    ArrayList<Product> m_cProductsList;

    public PotUserHomeDuskFragment2() {
        super();
    }

    public static PotUserHomeDuskFragment2 newInstance(int pPosition, String pKey) {
        PotUserHomeDuskFragment2 lPotUserHomeDuskFragment2 = new PotUserHomeDuskFragment2();

        Bundle args = new Bundle();
        args.putInt("Position", pPosition);
        args.putString("KEY", pKey);
        lPotUserHomeDuskFragment2.setArguments(args);

        return lPotUserHomeDuskFragment2;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        m_cIsActivityAttached = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_cObjMainView = inflater.inflate(R.layout.fragment_homeclasses, container, false);
        ButterKnife.bind(this, m_cObjMainView);

        m_cObjMainActivity.m_cObjFragmentBase = PotUserHomeDuskFragment2.this;

        return m_cObjMainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void handleUIMessage(Message pObjMessage) {

    }

    private void init() {
        m_cPos = getArguments().getInt("Position", 0);
        m_cKey = getArguments().getString("KEY");
        m_cProductsList = new ArrayList<>();
        m_cLayoutManager = new LinearLayoutManager(m_cObjMainActivity);
        m_cLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        m_cRecycClasses.setLayoutManager(m_cLayoutManager);
        m_cRecycClasses.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = m_cLayoutManager.getChildCount();
                    totalItemCount = m_cLayoutManager.getItemCount();
                    pastVisiblesItems = m_cLayoutManager.findFirstVisibleItemPosition();

//                    int page = totalItemCount / 15;
                    if (m_cLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            m_cLoading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
//                            int lpage = page + 1;
//                            page = lpage;
//                            doPagination(lpage);
                        }
                    }
                }
            }
        });

        //Calling Categories class api
        m_cObjMainActivity.displayProgressBar(-1, "");
        HashMap<String, String> lParams = new HashMap<>();
        lParams.put(Constants.TOKEN, Constants.VIRTUALDUSK);
        lParams.put(Constants.CATEGORY_ID, String.valueOf(2));
        placeRequest(Constants.PRODUCTS, Categories.class, null, lParams, null, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onAPIResponse(Object response, String apiMethod, Object refObj) {
        switch (apiMethod){
            case Constants.PRODUCTS:
                Categories lCategories = (Categories) response;
                if (lCategories != null && lCategories.getProducts().size() > 0) {
                    for (Product lProduct : lCategories.getProducts()) {
                        m_cProductsList.add(lProduct);
                    }
                    if (null != m_cProductsList && m_cProductsList.size() > 0) {
                        m_cRecycClassesAdapt = new CustomRecyclerAdapterForDusk2(m_cObjMainActivity, m_cProductsList, this);
                        m_cRecycClasses.setAdapter(m_cRecycClassesAdapt);
                    }
                }
                m_cObjMainActivity.hideDialog();
                break;
            default:
                super.onAPIResponse(response, apiMethod, refObj);
                m_cObjMainActivity.hideDialog();
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod, Object refObj) {
        switch (apiMethod) {
            default:
                if (apiMethod.contains(Constants.PRODUCTS)) {
                    m_cObjMainActivity.hideDialog();
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(m_cObjMainActivity, "Please check Network connection", Toast.LENGTH_SHORT).show();
                    } else {
                        String lMsg = new String(error.networkResponse.data);
                        m_cObjMainActivity.showErrorMsg(lMsg);
                    }
                } else {
                    super.onErrorResponse(error, apiMethod, refObj);
                    m_cObjMainActivity.hideDialog();
                }
                break;
        }
    }

    @Override
    public void onInfoClick(int pPostion, Product pProduct, View pView) {
        Intent lObjIntent = new Intent(m_cObjMainActivity, DuskProductScreen.class);
        lObjIntent.putExtra(DuskMacros.OBJ_PRODUCT, (new Gson()).toJson(pProduct));
        startActivity(lObjIntent);
    }

    @Override
    public void downloadImage(int pPostion, Product pProduct) {
        ((DuskUserScreen) m_cObjMainActivity).downloadAndUpdateProduct(pProduct);
    }
}