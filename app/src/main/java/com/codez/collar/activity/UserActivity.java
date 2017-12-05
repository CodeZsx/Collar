package com.codez.collar.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.ActivityUserBinding;
import com.codez.collar.fragment.StatusListFragment;
import com.codez.collar.fragment.UserAlbumFragment;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by codez on 2017/11/22.
 * Description:
 */

public class UserActivity extends BaseActivity<ActivityUserBinding> implements View.OnClickListener{
    public static final String INTENT_KEY_UID = "uid";
    public static final String INTENT_KEY_SCREEN_NAME = "screen_name";
    public static final String INTENT_KEY_TYPE = "type";
    public static final int INTENT_VALUE_UID = 1;
    public static final int INTENT_VALUE_SCREEN_NAME = 2;


    private UserBean mUserBean;
    private String uid;
    private String screen_name;
    @Override
    public int setContent() {
        return R.layout.activity_user;
    }

    @Override
    public void initView() {

        setStatusBarTranslucent();

        setSupportActionBar(mBinding.toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }


        uid = getIntent().getStringExtra(INTENT_KEY_UID);
        screen_name = getIntent().getStringExtra(INTENT_KEY_SCREEN_NAME);
        L.e("uid:"+uid+" screen_name:"+screen_name);


        if (uid != null && uid.equals(AccessTokenKeeper.getUid(this))) {
            mBinding.btnFollow.setVisibility(View.GONE);
        }

        HttpUtils.getInstance().getUserService(this)
                .getUserInfo(uid, screen_name)
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
                        mUserBean = userBean;
                        mBinding.setUser(userBean);
                    }
                });
        mBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {//展开状态

                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {//折叠状态
                    mBinding.collapsingToolbar.setTitleEnabled(true);
                    mBinding.toolbar.setBackgroundResource(R.color.colorToolbarBg);
                } else {//中间状态
                    mBinding.collapsingToolbar.setTitleEnabled(false);
                    mBinding.toolbar.setBackgroundColor(0x00000000);
                }
            }
        });

        mBinding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            String[] titles = {"微博","相册"};
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {

                    return new StatusListFragment().newInstance(uid, screen_name, StatusListFragment.VALUE_USER);
                }else{
                    return new UserAlbumFragment().newInstance(uid, screen_name);
                }
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        mBinding.tvFriends.setOnClickListener(this);
        mBinding.tvFriendsCount.setOnClickListener(this);
        mBinding.tvFollowers.setOnClickListener(this);
        mBinding.tvFollowersCount.setOnClickListener(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_remark) {
            return true;
        }else if (id == R.id.action_contact) {
            return true;
        }else if (id == R.id.action_defriend) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_friends:
            case R.id.tv_friends_count:
                startActivity(new Intent(this, FriendshipActivity.class)
                        .putExtra(FriendshipActivity.INTENT_TYPE, FriendshipActivity.TYPE_FRIENDS)
                        .putExtra(FriendshipActivity.INTENT_UID,mUserBean.getId()));
                break;
            case R.id.tv_followers:
            case R.id.tv_followers_count:
                startActivity(new Intent(this, FriendshipActivity.class)
                        .putExtra(FriendshipActivity.INTENT_TYPE, FriendshipActivity.TYPE_FOLLOWERS)
                        .putExtra(FriendshipActivity.INTENT_UID,mUserBean.getId()));
                break;
        }

    }
}
