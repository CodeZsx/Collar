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
import com.codez.collar.bean.CommentBean;
import com.codez.collar.databinding.ItemCommentBinding;
import com.codez.collar.ui.emojitextview.StatusContentTextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.BindingViewHolder> {

    private Context mContext;
    private List<CommentBean> list;

    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        ItemCommentBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_comment, parent, false);
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

        private ItemCommentBinding mBinding;

        public BindingViewHolder(ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.llRoot);
            this.mBinding = itemCommentBinding;
        }
        private void bindItem(final CommentBean bean){
            mBinding.setComment(bean);

            //微博正文
            mBinding.tvContent.setText(StatusContentTextUtil.getWeiBoContent(bean.getText(),
                    mContext, mBinding.tvContent));

            //点击事件
            mBinding.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:
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


    public void setList(List<CommentBean> list) {
        this.list.clear();
        this.list = list;
    }
    public void addAll(List<CommentBean> list){
        this.list.addAll(list);
    }
}
