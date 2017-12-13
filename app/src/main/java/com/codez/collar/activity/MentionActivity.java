package com.codez.collar.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.ActivityCommentBinding;
import com.codez.collar.fragment.CommentListFragment;
import com.codez.collar.fragment.StatusListFragment;

/**
 * Created by codez on 2017/11/22.
 * Description:
 */

public class MentionActivity extends BaseActivity<ActivityCommentBinding> implements View.OnClickListener{

    private UserBean mUserBean;
    private String uid;
    private String screen_name;

    private boolean isFollowMe = false;

    public int setContent() {
        return R.layout.activity_comment;
    }

    @Override
    public void initView() {

        setToolbarTitle(mBinding.toolbar, "@我的");

        mBinding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            String[] titles = {"微博","评论"};
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {

                    return new StatusListFragment().newInstance(null, null, StatusListFragment.VALUE_MENTION);
                }else{
                    return new CommentListFragment().newInstance(null, CommentListFragment.TYPE_COMMENT_MENTION);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }

    }
}
