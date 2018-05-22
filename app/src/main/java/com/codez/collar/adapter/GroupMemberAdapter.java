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
import com.codez.collar.activity.UserActivity;
import com.codez.collar.bean.Group;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.ItemFriendshipBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.T;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.BindingViewHolder> {

    private Context mContext;
    private List<UserBean> list;
    private String groupId;

    public GroupMemberAdapter(Context mContext, String groupId) {
        this.mContext = mContext;
        this.groupId = groupId;
        this.list = new ArrayList<>();
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
        runEnterAnimation(holder.itemView,position);
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
        private void bindItem(final UserBean bean, final int position){
            mBinding.setUser(bean);
            mBinding.btnFollow.setText("移除");
            mBinding.btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpUtils.getInstance().getFriendshipService()
                            .destoryGroupsMember(groupId, bean.getIdstr())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Group>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    T.s(mContext, "操作失败");
                                }

                                @Override
                                public void onNext(Group group) {
                                    T.s(mContext, "移除成功");
                                    list.remove(position);
                                    notifyDataSetChanged();
                                }
                            });
                }
            });
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
