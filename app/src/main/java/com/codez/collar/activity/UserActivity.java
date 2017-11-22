package com.codez.collar.activity;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.ActivityUserBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by codez on 2017/11/22.
 * Description:
 */

public class UserActivity extends BaseActivity<ActivityUserBinding>{
    @Override
    public int setContent() {
        return R.layout.activity_user;
    }

    @Override
    public void initView() {

        setSupportActionBar(mBinding.toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            actionBar.setDisplayShowTitleEnabled(false);
        }

        String uid = getIntent().getStringExtra("uid");
        HttpUtils.getInstance().getUserService(this)
                .getUserInfo(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserBean>() {
                    @Override
                    public void onCompleted() {
                        L.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError");
                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        L.e("onNext");
                        mBinding.setUser(userBean);
                    }
                });
        mBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {//展开状态
                    L.e("展开状态");
                    mBinding.ivHead.setVisibility(View.VISIBLE);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {//折叠状态
                    L.e("折叠状态");
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    mBinding.collapsingToolbar.setTitleEnabled(true);
                    mBinding.toolbar.setBackgroundColor(0x33000000);
                } else {//中间状态
                    L.e("中间状态");
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    mBinding.collapsingToolbar.setTitleEnabled(false);
                    mBinding.toolbar.setBackgroundColor(0x00ffffff);
                    mBinding.ivHead.setVisibility(View.GONE);
                }
            }
        });
    }
}
