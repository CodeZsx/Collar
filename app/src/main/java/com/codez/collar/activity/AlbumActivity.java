package com.codez.collar.activity;

import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivityAlbumBinding;

public class AlbumActivity extends BaseActivity<ActivityAlbumBinding> implements View.OnClickListener{

    @Override
    public int setContent() {
        return R.layout.activity_album;
    }

    @Override
    public void initView() {
        setToolbarTitle(mBinding.toolbar, "所有");

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
