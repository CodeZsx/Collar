package com.codez.collar.activity;

import android.content.Intent;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivitySetupBinding;

public class SetupActivity extends BaseActivity<ActivitySetupBinding> implements View.OnClickListener{

    public static final String INTENT_SCREENN_NAME = "screen_name";

    @Override
    public int setContent() {
        return R.layout.activity_setup;
    }

    @Override
    public void initView() {
        setToolbarTitle(mBinding.toolbar, "设置");

        mBinding.tvHint.setText(getIntent().getStringExtra(INTENT_SCREENN_NAME));

        mBinding.rlAccount.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_account:
                startActivity(new Intent(this, AccountActivity.class));
                break;

        }
    }
}
