package com.codez.collar.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.AlbumsBean;
import com.codez.collar.databinding.ActivityImageDetailBinding;
import com.codez.collar.fragment.ImageDetailFragment;
import com.codez.collar.utils.L;

public class ImageDetailActivity extends BaseActivity<ActivityImageDetailBinding> {

    public static final String INTENT_KEY_URL = "url";

    @Override
    public int setContent() {
        return R.layout.activity_image_detail;
    }

    @Override
    public void initView() {

        final AlbumsBean bean = (AlbumsBean) getIntent().getSerializableExtra(AlbumsBean.INTENT_SERIALIZABLE);

        final int size = bean.getPic_urls().size();

        if (size == 1) {
            mBinding.tabLayout.setVisibility(View.GONE);
        }
        mBinding.indicatorView.init(bean.getPic_urls().size());
        L.e("pos:"+bean.getCurPosition());
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
}
