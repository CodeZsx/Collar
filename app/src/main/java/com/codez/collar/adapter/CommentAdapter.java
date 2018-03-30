package com.codez.collar.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.codez.collar.R;
import com.codez.collar.activity.StatusDetailActivity;
import com.codez.collar.activity.UserActivity;
import com.codez.collar.bean.CommentBean;
import com.codez.collar.bean.StatusBean;
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
    private int mType;
    public static final int TYPE_COMMENT_NO_STATUS = 0;
    public static final int TYPE_COMMENT_STATUS = 1;


    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    public void setType(int mType) {
        this.mType = mType;
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
        runEnterAnimation(holder.itemView,position);
        holder.bindItem(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BindingViewHolder extends RecyclerView.ViewHolder {

        private ItemCommentBinding mBinding;

        public BindingViewHolder(ItemCommentBinding binding) {
            super(binding.llRoot);
            this.mBinding = binding;
        }
        private void bindItem(final CommentBean bean){
            mBinding.setComment(bean);

            //正文
            mBinding.tvContent.setText(StatusContentTextUtil.getWeiBoContent(bean.getText(),
                    mContext, mBinding.tvContent));

            if (bean.getUser().isVerified()) {
                mBinding.ivVip.setVisibility(View.VISIBLE);
            }

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
            //若是评论列表界面,则显示"回复按钮"和微博item
            if (mType == TYPE_COMMENT_STATUS) {
                mBinding.btnReply.setVisibility(View.VISIBLE);
                mBinding.btnReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                    }
                });
                mBinding.rlStatus.setVisibility(View.VISIBLE);
                mBinding.rlStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable(StatusBean.INTENT_SERIALIZABLE, bean.getStatus());
                        mContext.startActivity(new Intent(mContext, StatusDetailActivity.class)
                                .putExtras(mBundle));
                    }
                });
            }


            mBinding.executePendingBindings();
        }
    }

    //确保仅屏幕一开始能够容纳显示的item项才开启动画
    private boolean animationsLocked = false;
    private int lastAnimatedPosition = -1;
    private void runEnterAnimation(View view, int position) {
        if (animationsLocked)
            return;
        if (position > lastAnimatedPosition) {//lastAnimatedPosition是int类型变量，默认-1，
            //这两行代码确保了recyclerview滚动式回收利用视图时不会出现不连续效果
            lastAnimatedPosition = position;
            //起始状态
            view.setTranslationY(AnimationUtils.OFFSET_Y);
            view.setAlpha(0.f);
            view.animate()
                    //结束状态
                    .translationY(0).alpha(1.f)
                    .setStartDelay(AnimationUtils.DELAY_TIME * (position))//根据item的位置设置延迟时间,达到依次动画一个接一个进行的效果
                    .setInterpolator(new LinearInterpolator())//设置动画位移的效果
                    .setDuration(AnimationUtils.DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
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
