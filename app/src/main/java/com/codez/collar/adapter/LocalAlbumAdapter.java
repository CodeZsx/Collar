package com.codez.collar.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
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
    private int mSelectedCount;
    private List<String> mListSelected;
    private static final int SELECTED_COUNT = 9;

    public LocalAlbumAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
        this.mListSelected = new ArrayList<>();
        this.mSelectedCount = 0;
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
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }


    class BindingViewHolder extends RecyclerView.ViewHolder {

        private ItemLocalAlbumBinding mBinding;

        public BindingViewHolder(ItemLocalAlbumBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
        private void bindItem(final int postition){
            if (postition == 0) {
                //此处为拍照项，可点击跳转拍照界面
                mBinding.ivTakePhoto.setVisibility(View.VISIBLE);
                mBinding.rlPic.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(R.drawable.pic_album_take_photo)
                        .into(mBinding.ivTakePhoto);
                mBinding.ivTakePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Todo:跳转拍照界面
                        T.s(mContext, "TODO跳转拍照");
                    }
                });
            }else{
                mBinding.ivPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBinding.ivSelect.isSelected()) {
                            mBinding.ivSelect.setSelected(false);
                            mListSelected.remove(list.get(postition - 1));
                            mSelectedCount--;
                            onSelectedCountChangedListener.onChanged(mSelectedCount);
                        } else if (mSelectedCount == SELECTED_COUNT) {
                            T.s(mContext, "最多只能选择9张图片");
                        } else {
                            mBinding.ivSelect.setSelected(true);
                            mListSelected.add(list.get(postition - 1));
                            mSelectedCount++;
                            onSelectedCountChangedListener.onChanged(mSelectedCount);
                        }
                    }
                });
                mBinding.setUri(list.get(postition-1));
            }

            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) mBinding.getRoot().getLayoutParams();
            params.width = (int) (ScreenUtil.getScreenWidth(mContext)*0.31);
            params.height = (int) (ScreenUtil.getScreenWidth(mContext)*0.31);
            mBinding.getRoot().setLayoutParams(params);

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
    public List<String> getListSelected() {
        return mListSelected;
    }


    private OnSelectedCountChangedListener onSelectedCountChangedListener;
    public void addSelectedCountChangedListener(OnSelectedCountChangedListener listener) {
        this.onSelectedCountChangedListener = listener;
    }
    public interface OnSelectedCountChangedListener {
        void onChanged(int count);
    }
}
