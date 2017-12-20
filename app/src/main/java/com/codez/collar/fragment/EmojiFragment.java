package com.codez.collar.fragment;

import android.os.Bundle;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.databinding.FragmentEmojiBinding;

/**
 * Created by codez on 2017/12/21.
 * Description:
 */

public class EmojiFragment extends BaseFragment<FragmentEmojiBinding> {

    public static EmojiFragment Instance() {
        EmojiFragment instance = new EmojiFragment();
        Bundle bundle = new Bundle();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public int setContent() {
        return R.layout.fragment_emoji;
    }

    @Override
    public void initView(View root) {

    }
}
