package com.codez.collar.activity;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivitySearchBinding;
import com.codez.collar.event.SearchWordChangedEvent;
import com.codez.collar.fragment.SearchComprehensiveFragment;
import com.codez.collar.fragment.UserListFragment;
import com.codez.collar.utils.EventBusUtils;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> implements View.OnClickListener{

    private static final String TAG = "SearchActivity";

    @Override
    public int setContent() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        EventBusUtils.register(this);
        Drawable leftDrawable = mBinding.etSearch.getCompoundDrawables()[0];
        if (leftDrawable != null) {
            leftDrawable.setBounds(0,0,65,65);
            mBinding.etSearch.setCompoundDrawables(leftDrawable, mBinding.etSearch.getCompoundDrawables()[1],
                    mBinding.etSearch.getCompoundDrawables()[2], mBinding.etSearch.getCompoundDrawables()[3]);
        }

        mBinding.ivBack.setOnClickListener(this);

        mBinding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            String[] titles = {"综合","用户"};
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new SearchComprehensiveFragment();
                }else if (position == 1) {
                    return new UserListFragment();
                }else{
                    return null;
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
        mBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        mBinding.tabLayout.setVisibility(View.GONE);
        mBinding.llPreview.setVisibility(View.VISIBLE);

    }

    private void search() {
        mBinding.tabLayout.setVisibility(View.VISIBLE);
        mBinding.llPreview.setVisibility(View.GONE);
        String word = mBinding.etSearch.getText().toString();
        if (word != null && !"".equals(word)) {
            Log.i(TAG, "search:" + word);
            EventBusUtils.sendEvent(new SearchWordChangedEvent(word));
        }
    }

    @Override
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        super.onDestroy();
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
