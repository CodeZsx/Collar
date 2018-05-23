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
import com.codez.collar.activity.LoginActivity;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.auth.AccessTokenManager;
import com.codez.collar.base.BaseApp;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.ItemAccountBinding;
import com.codez.collar.service.CoreService;

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
        runEnterAnimation(holder.itemView, position);
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
                mBinding.rlAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    }
                });
            }
            //若当前加载的账号即为登录账号，则设置选中状态
            if (AccessTokenKeeper.getInstance().getUid().equals(bean.getId())){
                mBinding.ivAccountChecked.setSelected(true);
            }
            mBinding.ivAccountChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mBinding.ivAccountChecked.isSelected()) {
                        //切换token
                        AccessTokenManager accessTokenManager = new AccessTokenManager();
                        accessTokenManager.switchToken(mContext, bean.getIdstr());
                        //重启service
                        mContext.startService(new Intent(mContext, CoreService.class));
                        //重启应用
                        final Intent intent = BaseApp.sContext.getPackageManager()
                                .getLaunchIntentForPackage(BaseApp.getAppPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        BaseApp.sContext.startActivity(intent);
                    }
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
