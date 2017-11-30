package com.codez.collar.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;
import com.codez.collar.databinding.ItemLocalAlbumBinding;
import com.codez.collar.utils.ScreenUtil;
import com.codez.collar.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class LocalAlbumAdapter extends RecyclerView.Adapter<LocalAlbumAdapter.BindingViewHolder> {

    private Context mContext;
    private List<String> list;

    public LocalAlbumAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        ItemLocalAlbumBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_local_album, parent, false);

        return new BindingViewHolder(mBinding);
    }


    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        holder.bindItem(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class BindingViewHolder extends RecyclerView.ViewHolder {

        private ItemLocalAlbumBinding mBinding;

        public BindingViewHolder(ItemLocalAlbumBinding binding) {
            super(binding.llRoot);
            this.mBinding = binding;
        }
        private void bindItem(final String uri, final int postition){
            mBinding.ivPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T.s(mContext, "click:"+uri);
                }
            });
            mBinding.setUri(uri);
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) mBinding.llRoot.getLayoutParams();
            params.width = (int) (ScreenUtil.getScreenWidth(mContext)*0.31);
            params.height = (int) (ScreenUtil.getScreenWidth(mContext)*0.31);
            mBinding.llRoot.setLayoutParams(params);

            mBinding.executePendingBindings();
        }
    }

    public void setList(List<String> list) {
        this.list.clear();
        this.list = list;
    }
    public void addAll(List<String> list){
        this.list.addAll(list);
    }

    public List<String> getList() {
        return list;
    }
}
