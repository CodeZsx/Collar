package com.codez.collar.activity;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivitySetupBinding;

public class SetupActivity extends BaseActivity<ActivitySetupBinding> {

    @Override
    public int setContent() {
        return R.layout.activity_setup;
    }

    @Override
    public void initView() {
//        Toolbar t = mBinding.toolbar;
//        setSupportActionBar(mBinding.toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onBackPressed();
//                }
//            });
//            actionBar.setTitle("设置");
//        }
        setToolbarTitle(mBinding.toolbar, "设置");
    }

}
