package com.codez.collar.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;
import com.codez.collar.activity.UserActivity;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.databinding.ItemStatusBinding;
import com.codez.collar.ui.emojitextview.StatusContentTextUtil;
import com.codez.collar.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.BindingViewHolder> {

    private Context mContext;
    private List<StatusBean> list;

    public StatusAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        ItemStatusBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_status, parent, false);
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

        private ItemStatusBinding mBinding;

        public BindingViewHolder(ItemStatusBinding itemStatusBinding) {
            super(itemStatusBinding.llRoot);
            this.mBinding = itemStatusBinding;
        }
        private void bindItem(final StatusBean bean){
            mBinding.setStatus(bean);

            mBinding.tvContent.setText(StatusContentTextUtil.getWeiBoContent(bean.getText(),
                    mContext, mBinding.tvContent));

            mBinding.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:跳转status详情页
                    T.s(mContext, "跳转详情页");
                }
            });
            mBinding.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, UserActivity.class)
                    .putExtra(UserActivity.INTENT_KEY_UID, bean.getUser().getId()));
                }
            });
            mBinding.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, UserActivity.class)
                            .putExtra(UserActivity.INTENT_KEY_UID, bean.getUser().getId()));
                }
            });
            mBinding.executePendingBindings();
        }
    }

    public void setList(List<StatusBean> list) {
        this.list.clear();
        this.list = list;
    }
    public void addAll(List<StatusBean> list){
        this.list.addAll(list);
    }
}
