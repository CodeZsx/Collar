package com.codez.collar.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.codez.collar.R;

/**
 * Created by codez on 2017/11/20.
 * Description:
 */

public abstract class BaseFragment<VD extends ViewDataBinding> extends Fragment{
    //布局view
    protected VD mBinding;
    //fragment是否显示
    protected boolean mIsVisible = false;
    //加载中
    private LinearLayout mLlProgressBar;
    //加载失败
    private LinearLayout mRefresh;
    //动画
    private AnimationDrawable mAnimationDrawable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_base, null);
        mBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                setContent(), null, false);
        mBinding.getRoot().setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((RelativeLayout) root.findViewById(R.id.container))
                .addView(mBinding.getRoot());
        initView(root);
        return root;
    }

    //返回布局文件id
    public abstract int setContent();

    public abstract void initView(View root);
}
