package com.codez.collar.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;
import com.codez.collar.activity.DirectMsgActivity;
import com.codez.collar.bean.DirectMsgUserlistBean;
import com.codez.collar.databinding.ItemDirectMsgBinding;
import com.codez.collar.databinding.ItemDirectMsgHeaderBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class DirectMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<DirectMsgUserlistBean> list;
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_DIRECT_MSG = 0;

    public DirectMsgAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == VIEW_TYPE_HEADER) {
            ItemDirectMsgHeaderBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_direct_msg_header, parent, false);
            return new HeaderViewHolder(mBinding);
        }else{
            ItemDirectMsgBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_direct_msg, parent, false);
            return new DirectMsgViewHolder(mBinding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_DIRECT_MSG;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bindItem();
        }else{
            ((DirectMsgViewHolder) holder).bindItem(list.get(position-1),position-1);
        }

    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{

        private ItemDirectMsgHeaderBinding mBinding;

        public HeaderViewHolder(ItemDirectMsgHeaderBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bindItem() {
            mBinding.rlAt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mBinding.rlComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mBinding.rlLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    class DirectMsgViewHolder extends RecyclerView.ViewHolder {

        private ItemDirectMsgBinding mBinding;

        public DirectMsgViewHolder(ItemDirectMsgBinding binding) {
            super(binding.llRoot);
            this.mBinding = binding;
        }
        private void bindItem(final DirectMsgUserlistBean bean, int position){
            mBinding.setMsg(bean.getDirect_message());
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, DirectMsgActivity.class)
                            .putExtra(DirectMsgActivity.INTENT_UID, bean.getUser().getId())
                            .putExtra(DirectMsgActivity.INTENT_SCREEN_NAME, bean.getUser().getScreen_name()));
                }
            });
            mBinding.executePendingBindings();
        }
    }


    public void setList(List<DirectMsgUserlistBean> list) {
        this.list.clear();
        this.list = list;
    }

    public void add(DirectMsgUserlistBean bean) {
        this.list.add(bean);
    }
    public void addAll(List<DirectMsgUserlistBean> list){
        this.list.addAll(list);
    }
}
