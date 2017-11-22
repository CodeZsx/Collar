package com.codez.collar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.codez.collar.databinding.ItemWeiboBinding;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class WeiboAdapter extends RecyclerView.Adapter<WeiboAdapter.BindingViewHolder> {

    private Context mContext;


    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class BindingViewHolder extends RecyclerView.ViewHolder {

        private ItemWeiboBinding itemWeiboBinding;

        public BindingViewHolder(ItemWeiboBinding itemWeiboBinding) {
            super(itemWeiboBinding.llRoot);
            this.itemWeiboBinding = itemWeiboBinding;
        }
    }
}
