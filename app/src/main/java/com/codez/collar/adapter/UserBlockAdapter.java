package com.codez.collar.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;
import com.codez.collar.activity.UserActivity;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.BlockUserBinding;
import com.codez.collar.databinding.ItemUserAlbumBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class UserBlockAdapter extends RecyclerView.Adapter<UserBlockAdapter.BindingViewHolder> {

    private static final String TAG = "UserBlockAdapter";
    private Context mContext;
    private List<UserBean> list;
    private ItemUserAlbumBinding imgSize;

    public UserBlockAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        BlockUserBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.block_user, parent, false);

        return new BindingViewHolder(mBinding);
    }


    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        holder.bindItem(list, position);
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size() + 1;
    }


    class BindingViewHolder extends RecyclerView.ViewHolder {

        private BlockUserBinding mBinding;

        public BindingViewHolder(BlockUserBinding binding) {
            super(binding.llRoot);
            this.mBinding = binding;
        }
        private void bindItem(List<UserBean> list, final int postition){
            if (postition == list.size()) {
                Log.i(TAG, "p=size:" + (onMoreUserClickListener == null));
                mBinding.llRoot.setOnClickListener(onMoreUserClickListener);
                mBinding.ivHead.setImageResource(R.drawable.ic_more_user);
                mBinding.tvScreenName.setText("更多");
            }else{
                final UserBean bean = list.get(postition);
                mBinding.llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, UserActivity.class)
                                .putExtra(UserActivity.INTENT_KEY_UID, bean.getId()));
                    }
                });
                mBinding.setUser(bean);
                mBinding.executePendingBindings();
            }
        }
    }

    private View.OnClickListener onMoreUserClickListener = null;
    public void setOnMoreUserClickListener(View.OnClickListener listener) {
        onMoreUserClickListener = listener;
    }

    public void setList(List<UserBean> list) {
        this.list.clear();
        this.list = list;
    }
    public void addAll(List<UserBean> list){
        this.list.addAll(list);
    }

    public List<UserBean> getList() {
        return list;
    }
}
