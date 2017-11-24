package com.codez.collar.activity;

import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivityImageDetailBinding;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageDetailActivity extends BaseActivity<ActivityImageDetailBinding> {

    public static final String INTENT_KEY_URL = "url";

    @Override
    public int setContent() {
        return R.layout.activity_image_detail;
    }

    @Override
    public void initView() {

        setStatusBarTranslucent();

        String url = getIntent().getStringExtra(INTENT_KEY_URL);
        mBinding.setUrl(url);
        mBinding.photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                ImageDetailActivity.this.finish();
            }
        });

    }
}
