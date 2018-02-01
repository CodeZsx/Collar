package com.codez.collar.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.ItemAccountBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.BindingViewHolder> {

    private Context mContext;
    private List<UserBean> list;

    public AccountAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        ItemAccountBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_account, parent, false);
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

        private ItemAccountBinding mBinding;

        public BindingViewHolder(ItemAccountBinding binding) {
            super(binding.llRoot);
            this.mBinding = binding;
        }
        private void bindItem(final UserBean bean, int position){
            mBinding.setUser(bean);
            //若当前是最后一个账号item，则显示添加账号item
            if (position == list.size() - 1) {
                mBinding.rlAdd.setVisibility(View.VISIBLE);
            }
            //若当前加载的账号即为登录账号，则设置选中状态
            if (AccessTokenKeeper.getInstance().getUid().equals(bean.getId())){
                mBinding.ivAccountChecked.setSelected(true);
            }
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
