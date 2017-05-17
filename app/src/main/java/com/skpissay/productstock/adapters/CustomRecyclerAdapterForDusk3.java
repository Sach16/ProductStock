package com.skpissay.productstock.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skpissay.productstock.R;
import com.skpissay.productstock.interfaces.DuskListener;
import com.skpissay.productstock.macros.DuskMacros;
import com.skpissay.productstock.models.Product;
import com.skpissay.productstock.models.ProductTable;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by S.K. Pissay on 17/05/17.
 */
public class CustomRecyclerAdapterForDusk3 extends RecyclerView.Adapter{

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static DuskListener m_cClickListener;
    private static List<Product> m_cObjJsonUsers;
    private Context m_cObjContext;

    public CustomRecyclerAdapterForDusk3(Context pContext, List<Product> pJsonUsers,
                                         DuskListener pListener) {
        this.m_cObjContext = pContext;
        this.m_cObjJsonUsers = pJsonUsers;
        this.m_cClickListener = pListener;
    }

    @Override
    public int getItemCount() {
        return m_cObjJsonUsers.size();
    }

    @Override
    public int getItemViewType(int position) {
        return m_cObjJsonUsers.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View lView;
        // paging logic
        if (viewType == VIEW_ITEM) {
            lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell, parent, false);
            DataObjectHolder ldataObjectHolder = new DataObjectHolder(lView);
            vh = ldataObjectHolder;
        } else {
            lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressdialog_paging, parent, false);
            ProgressViewHolder lprogressViewHolder = new ProgressViewHolder(lView);
            vh = lprogressViewHolder;
        }
        return vh;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable
        @BindView(R.id.main_ll)
        LinearLayout cvUserCell;

        @Nullable
        @BindView(R.id.image_view)
        ImageView image;

        @Nullable
        @BindView(R.id.title_text)
        TextView title;

        @Nullable
        @BindView(R.id.desc_text)
        TextView desc;

        public DataObjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Optional
        @OnClick({R.id.main_ll})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_ll:
                    m_cClickListener.onInfoClick(getPosition(), m_cObjJsonUsers.get(getPosition()), v);
                    break;
            }
        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.progressBar1)
        ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DataObjectHolder) {
            ProductTable lProductTable = null;
            try {
                lProductTable = ProductTable.find(ProductTable.class, "category_id = ? and product_id = ?",
                        String.valueOf(m_cObjJsonUsers.get(position).getCategoryId()),
                        String.valueOf(m_cObjJsonUsers.get(position).getTblProductId())).get(0);
            } catch (IndexOutOfBoundsException e) {
                ((DataObjectHolder) holder).image.setImageResource(R.drawable.profile_placeholder);
                e.printStackTrace();
            } catch (Exception e) {
                ((DataObjectHolder) holder).image.setImageResource(R.drawable.profile_placeholder);
                e.printStackTrace();
            } finally {
                if (lProductTable != null){
                    Picasso.with(m_cObjContext)
                            .load(new File(lProductTable.getImage()))
                            .error(R.drawable.profile_placeholder)
                            .placeholder(R.drawable.profile_placeholder)
                            .fit()
                            .into(((DataObjectHolder) holder).image);
                } else {
                    m_cClickListener.downloadImage(position, m_cObjJsonUsers.get(position));
                    Picasso.with(m_cObjContext)
                            .load(DuskMacros.checkAndUpdateUrl(m_cObjJsonUsers.get(position).getImage()))
                            .error(R.drawable.profile_placeholder)
                            .placeholder(R.drawable.profile_placeholder)
                            .fit()
                            .into(((DataObjectHolder) holder).image);
                }
            }

            try {
                ((DataObjectHolder) holder).title.setText(m_cObjJsonUsers.get(position).getProductName());
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                ((DataObjectHolder) holder).desc.setText(m_cObjJsonUsers.get(position).getDescription());
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

}
