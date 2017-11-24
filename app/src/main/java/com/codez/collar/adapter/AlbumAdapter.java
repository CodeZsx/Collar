package com.codez.collar.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;
import com.codez.collar.activity.ImageDetailActivity;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.databinding.ItemAlbumBinding;
import com.codez.collar.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.BindingViewHolder> {

    private Context mContext;
    private List<StatusBean.PicUrlsBean> list;
    private ItemAlbumBinding imgSize;

    public AlbumAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        ItemAlbumBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_album, parent, false);

        return new BindingViewHolder(mBinding);
    }


    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        holder.bindItem(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class BindingViewHolder extends RecyclerView.ViewHolder {

        private ItemAlbumBinding mBinding;

        public BindingViewHolder(ItemAlbumBinding itemStatusBinding) {
            super(itemStatusBinding.llRoot);
            this.mBinding = itemStatusBinding;
        }
        private void bindItem(final StatusBean.PicUrlsBean bean){
            mBinding.ivPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ImageDetailActivity.class)
                    .putExtra(ImageDetailActivity.INTENT_KEY_URL, bean.getThumbnail_pic()));
                }
            });

            mBinding.setBean(bean);
            setImgSize(mBinding, bean);
            mBinding.executePendingBindings();
        }
    }

    private void setImgSize(ItemAlbumBinding mBinding, StatusBean.PicUrlsBean bean) {

        if (list.size() == 1) {
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) mBinding.llRoot.getLayoutParams();
            params.width = (int) (ScreenUtil.getScreenWidth(mContext) * 0.5);
            params.height = (int) (ScreenUtil.getScreenWidth(mContext) * 0.5);
            mBinding.llRoot.setLayoutParams(params);
        }else{
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) mBinding.llRoot.getLayoutParams();
            params.width = (int) (ScreenUtil.getScreenWidth(mContext)*0.28);
            params.height = (int) (ScreenUtil.getScreenWidth(mContext)*0.28);
            mBinding.llRoot.setLayoutParams(params);
        }


    }

    public void setList(List<StatusBean.PicUrlsBean> list) {
        this.list.clear();
        this.list = list;
    }
    public void addAll(List<StatusBean.PicUrlsBean> list){
        this.list.addAll(list);
    }

    public List<StatusBean.PicUrlsBean> getList() {
        return list;
    }
}
