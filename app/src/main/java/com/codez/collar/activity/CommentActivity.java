package com.codez.collar.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.CommentAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.ActivityCommentBinding;
import com.codez.collar.fragment.CommentListFragment;

/**
 * Created by codez on 2017/11/22.
 * Description:
 */

public class CommentActivity extends BaseActivity<ActivityCommentBinding> implements View.OnClickListener{

    private UserBean mUserBean;
    private String uid;
    private String screen_name;

    private boolean isFollowMe = false;

    public int setContent() {
        return R.layout.activity_comment;
    }

    @Override
    public void initView() {

        setToolbarTitle(mBinding.toolbar, "评论");

        mBinding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            String[] titles = {"收到的评论","发出的评论"};
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {

                    return new CommentListFragment().newInstance(null, CommentAdapter.TYPE_COMMENT_TO_ME);
                }else{
                    return new CommentListFragment().newInstance(null, CommentAdapter.TYPE_COMMENT_BY_ME);
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
