package com.codez.collar.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;
import com.codez.collar.activity.ThemeActivity;
import com.codez.collar.databinding.ItemThemeBinding;
import com.codez.collar.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.BindingViewHolder> {

    private Context mContext;
    private List<ThemeActivity.ThemeBean> list;

    public ThemeAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        ItemThemeBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_theme, parent, false);

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

        private ItemThemeBinding mBinding;

        public BindingViewHolder(ItemThemeBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
        private void bindItem(final ThemeActivity.ThemeBean bean, final int postition){
            //设置宽高
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mBinding.rlTheme.getLayoutParams();
            params.width = (int) (ScreenUtil.getScreenWidth(mContext) * 0.2);
            params.height = params.width * 4 / 3;
            mBinding.rlTheme.setLayoutParams(params);

            mBinding.rlTheme.setBackgroundResource(mContext.getResources()
                    .getIdentifier("colorPrimary_" + bean.getTheme(), "color", mContext.getPackageName()));
            mBinding.ivCheck.setVisibility(bean.isSelected() ? View.VISIBLE : View.GONE);

            mBinding.rlTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!bean.isSelected()) {
                        clearSelected();
                        bean.setSelected(true);
                        notifyDataSetChanged();
                    }
                }
            });
            mBinding.executePendingBindings();
        }
    }

    private void clearSelected() {
        for (ThemeActivity.ThemeBean bean : list) {
            if (bean.isSelected()) {
                bean.setSelected(false);
            }
        }
    }

    public void setList(List<ThemeActivity.ThemeBean> list) {
        this.list.clear();
        this.list = list;
    }
    public void addAll(List<ThemeActivity.ThemeBean> list){
        this.list.addAll(list);
    }

    public List<ThemeActivity.ThemeBean> getList() {
        return list;
    }

    public String getCurTheme() {
        for (ThemeActivity.ThemeBean bean : list) {
            if (bean.isSelected()) {
                return bean.getTheme();
            }
        }
        return list.get(0).getTheme();
    }
}
