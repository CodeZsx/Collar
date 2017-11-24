package com.codez.collar.fragment;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.CompoundButton;

import com.codez.collar.Config;
import com.codez.collar.R;
import com.codez.collar.activity.SetupActivity;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.databinding.FragmentMineBinding;

import skin.support.SkinCompatManager;


public class MineFragment extends BaseFragment<FragmentMineBinding> implements View.OnClickListener {


    @Override
    public int setContent() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(View root) {
        mBinding.switchButton.setChecked(Config.getCachedNight(getContext()));
        mBinding.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
                    }
                    Config.cacheNight(getContext(), false);
                }else {
                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                    Config.cacheNight(getContext(), true);
                }
            }
        });
        mBinding.ivSetup.setOnClickListener(this);
        initData();
    }

    private void initData(){

    }
    private void refreshNews() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setup:
                startActivity(new Intent(getActivity(), SetupActivity.class));
                break;
        }

    }
}
