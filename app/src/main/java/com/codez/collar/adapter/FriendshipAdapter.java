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
import com.codez.collar.databinding.ItemFriendshipBinding;
import com.codez.collar.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class FriendshipAdapter extends RecyclerView.Adapter<FriendshipAdapter.BindingViewHolder> {

    private Context mContext;
    private List<UserBean> list;
    private String mType;
    private List<String> mFriendsIds;

    public FriendshipAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public List<String> getFriendsIds() {
        return mFriendsIds;
    }

    public void setFriendsIds(List<String> mFriendsIds) {
        this.mFriendsIds = mFriendsIds;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        ItemFriendshipBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_friendship, parent, false);
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

        private ItemFriendshipBinding mBinding;

        public BindingViewHolder(ItemFriendshipBinding binding) {
            super(binding.llRoot);
            this.mBinding = binding;
        }
        private void bindItem(final UserBean bean, int position){
            mBinding.setUser(bean);
            L.e(bean.getIdstr());
            if (mFriendsIds.contains(bean.getIdstr())){
                if (bean.isFollow_me()) {
                    mBinding.btnFollow.setText("互相关注");
                }else{
                    mBinding.btnFollow.setText("已关注");
                }
            }
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
