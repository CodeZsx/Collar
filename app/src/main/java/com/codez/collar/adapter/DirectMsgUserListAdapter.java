package com.codez.collar.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.codez.collar.R;
import com.codez.collar.activity.CommentActivity;
import com.codez.collar.activity.DirectMsgActivity;
import com.codez.collar.activity.MentionActivity;
import com.codez.collar.bean.DirectMsgUserlistBean;
import com.codez.collar.databinding.ItemDirectMsgBinding;
import com.codez.collar.databinding.ItemDirectMsgHeaderBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class DirectMsgUserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<DirectMsgUserlistBean> list;
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_DIRECT_MSG = 0;

    public DirectMsgUserListAdapter(Context mContext) {
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
            runEnterAnimation(holder.itemView,position);
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
                    mContext.startActivity(new Intent(mContext, MentionActivity.class));
                }
            });
            mBinding.rlComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, CommentActivity.class));
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
        private void bindItem(final DirectMsgUserlistBean bean, final int position){
            mBinding.setUserlist(bean);
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, DirectMsgActivity.class)
                            .putExtra(DirectMsgActivity.INTENT_UID, bean.getUser().getId())
                            .putExtra(DirectMsgActivity.INTENT_SCREEN_NAME, bean.getUser().getScreen_name()));
                    //若此用户有未读私信，即未读消息提示气泡Visible，则需使其Gone
                    mBinding.tvNoticeNum.setVisibility(View.GONE);
                    //并修改此用户的未读私信数
                    list.get(position).setUnread_count(0);
                }
            });
            if (bean.getUnread_count() > 0) {
                mBinding.tvNoticeNum.setVisibility(View.VISIBLE);
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

    public void setList(List<DirectMsgUserlistBean> list) {
        this.list.clear();
        this.list = list;
    }

    public List<DirectMsgUserlistBean> getList() {
        return list;
    }

    public void add(DirectMsgUserlistBean bean) {
        this.list.add(bean);
    }
    public void addAll(List<DirectMsgUserlistBean> list){
        this.list.addAll(list);
    }
}
