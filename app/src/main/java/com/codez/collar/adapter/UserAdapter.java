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
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.ItemUserBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.BindingViewHolder> {

    private Context mContext;
    private List<UserBean> list;
    private String mType;

    public UserAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        ItemUserBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_user, parent, false);
        return new BindingViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        holder.bindItem(list.get(position),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BindingViewHolder extends RecyclerView.ViewHolder {

        private ItemUserBinding mBinding;

        public BindingViewHolder(ItemUserBinding binding) {
            super(binding.llRoot);
            this.mBinding = binding;
        }
        private void bindItem(final UserBean bean, int position){
            mBinding.setUser(bean);
            mBinding.tvFollowersCount.setText("粉丝："+bean.getFollowers_count());
            mBinding.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, UserActivity.class)
                    .putExtra(UserActivity.INTENT_KEY_UID, bean.getId()));
                }
            });
            mBinding.executePendingBindings();
        }
    }


    public void setList(List<UserBean> list) {
        this.list.clear();
        this.list = list;
    }

    public void add(UserBean bean) {
        this.list.add(bean);
    }
    public void addAll(List<UserBean> list){
        this.list.addAll(list);
    }
}
