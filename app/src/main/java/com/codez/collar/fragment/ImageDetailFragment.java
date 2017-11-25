package com.codez.collar.fragment;

import android.os.Bundle;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.databinding.FragmentImageDetailBinding;


public class ImageDetailFragment extends BaseFragment<FragmentImageDetailBinding> implements View.OnClickListener {

    private static final String KEY_URL = "url";

    private String mUrl;
    private int curPage;
    @Override
    public int setContent() {
        return R.layout.fragment_image_detail;
    }

    public static ImageDetailFragment newInstance(String url){
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initView(View root) {
        if (getArguments() != null) {
            mUrl = getArguments().getString(KEY_URL);
        }
        curPage = 1;
        mBinding.setUrl(mUrl);

    }


    @Override
    public void onClick(View v) {

    }
}
