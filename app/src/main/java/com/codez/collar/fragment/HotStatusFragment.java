package com.codez.collar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.databinding.FragmentBaseBinding;

/**
 * Created by codez on 2017/11/20.
 * Description:
 */

public class HotStatusFragment extends BaseFragment<FragmentBaseBinding> {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public int setContent() {
        return R.layout.fragment_hot;
    }

    @Override
    public void initView(View root) {

    }


}
