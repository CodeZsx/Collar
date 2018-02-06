package com.codez.collar.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.AlbumsBean;
import com.codez.collar.databinding.ActivityImageDetailBinding;
import com.codez.collar.fragment.ImageDetailFragment;
import com.codez.collar.utils.ScreenUtil;

public class ImageDetailActivity extends BaseActivity<ActivityImageDetailBinding> {

    private static final String TAG = "ImageDetailActivity";
    public static final String INTENT_KEY_URL = "url";

    @Override
    public int setContent() {
        return R.layout.activity_image_detail;
    }

    @Override
    public void initView() {
        setSwipeBackEnable(false);
        startZoomAnim();

        final AlbumsBean bean = (AlbumsBean) getIntent().getSerializableExtra(AlbumsBean.INTENT_SERIALIZABLE);

        final int size = bean.getPic_urls().size();

        if (size == 1) {
            mBinding.tabLayout.setVisibility(View.GONE);
        }
        mBinding.indicatorView.init(bean.getPic_urls().size());
        Log.i(TAG, "pos:"+bean.getCurPosition());
        mBinding.indicatorView.playTo(bean.getCurPosition());

        mBinding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return new ImageDetailFragment().newInstance(bean.getPic_urls().get(position).getThumbnail_pic());
            }

            @Override
            public int getCount() {
                return size;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "#";
            }
        });
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPos = bean.getCurPosition();
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.indicatorView.playBy(oldPos, position);
                oldPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.viewPager.setCurrentItem(bean.getCurPosition());
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

    }

    private void startZoomAnim() {
        if (savedInstanceState == null) {
            final int x = getIntent().getIntExtra("locationX", 100);
            final int y = getIntent().getIntExtra("locationY", 100);
            mBinding.llRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mBinding.llRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    ScaleAnimation animation = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f, x, y);
                    animation.setDuration(200);
                    mBinding.llRoot.startAnimation(animation);
                    return true;
                }
            });
        }
    }

    @Override
    public void finish() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, ScreenUtil.getScreenHeight(ImageDetailActivity.this));
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ImageDetailActivity.super.finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBinding.llRoot.startAnimation(animation);

    }
}
