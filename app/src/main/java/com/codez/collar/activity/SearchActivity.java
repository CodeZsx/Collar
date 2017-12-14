package com.codez.collar.activity;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivitySearchBinding;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> implements View.OnClickListener{


    @Override
    public int setContent() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {

        Drawable leftDrawable = mBinding.etSearch.getCompoundDrawables()[0];
        if (leftDrawable != null) {
            leftDrawable.setBounds(0,0,65,65);
            mBinding.etSearch.setCompoundDrawables(leftDrawable, mBinding.etSearch.getCompoundDrawables()[1],
                    mBinding.etSearch.getCompoundDrawables()[2], mBinding.etSearch.getCompoundDrawables()[3]);
        }

        mBinding.ivBack.setOnClickListener(this);

    }

    private void loadData() {
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
        }
    }
}
