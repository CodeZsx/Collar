package com.codez.collar.activity;

import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivityPostBinding;

public class PostActivity extends BaseActivity<ActivityPostBinding> implements View.OnClickListener{

    public static final String INTENT_REPOST = "repost";

    private boolean isRepost;

    @Override
    public int setContent() {
        return R.layout.activity_post;
    }

    @Override
    public void initView() {
        isRepost = getIntent().getBooleanExtra(INTENT_REPOST, false);
        if (isRepost) {
            setToolbarTitle(mBinding.toolbar, "转发微博");
        }else{
            setToolbarTitle(mBinding.toolbar, "发布微博");
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
