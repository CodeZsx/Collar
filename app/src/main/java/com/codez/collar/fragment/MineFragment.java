package com.codez.collar.fragment;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.CompoundButton;

import com.codez.collar.Config;
import com.codez.collar.R;
import com.codez.collar.activity.FavoriteActivity;
import com.codez.collar.activity.SetupActivity;
import com.codez.collar.activity.UserActivity;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.FragmentMineBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
        mBinding.rlHeader.setOnClickListener(this);
        mBinding.blockStatus.setOnClickListener(this);
        mBinding.blockFriend.setOnClickListener(this);
        mBinding.blockFollower.setOnClickListener(this);
        mBinding.itemFavorite.setOnClickListener(this);
        initData();
    }

    private void initData(){
        HttpUtils.getInstance().getUserService(getContext())
                .getUserInfo(AccessTokenKeeper.getUid(getContext()), null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserBean>() {
                    @Override
                    public void onCompleted() {
                        L.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError");
                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        L.e("onNext");
                        mBinding.setUser(userBean);
                    }
                });

    }
    private void refreshNews() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setup:
                startActivity(new Intent(getActivity(), SetupActivity.class)
                .putExtra(SetupActivity.INTENT_SCREENN_NAME, mBinding.tvSreenName.getText().toString()));
                break;
            case R.id.rl_header:
            case R.id.block_status:
                startActivity(new Intent(getActivity(), UserActivity.class)
                        .putExtra(UserActivity.INTENT_KEY_UID, AccessTokenKeeper.getUid(getContext())));
                break;
            case R.id.item_favorite:
                startActivity(new Intent(getActivity(), FavoriteActivity.class));
                break;
        }

    }
}
