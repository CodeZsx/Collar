package com.codez.collar.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;

import com.codez.collar.Config;
import com.codez.collar.R;
import com.codez.collar.activity.FavoriteActivity;
import com.codez.collar.activity.FriendshipActivity;
import com.codez.collar.activity.SetupActivity;
import com.codez.collar.activity.ThemeActivity;
import com.codez.collar.activity.UserActivity;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.FragmentMineBinding;
import com.codez.collar.event.RefreshStatusBarEvent;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;

import org.greenrobot.eventbus.EventBus;

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

        mBinding.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    //取消夜间模式后，主题恢复为之前的存储的主题
                    SkinCompatManager.getInstance().loadSkin(Config.getCachedTheme(getContext()),SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                    Config.cacheNight(getContext(), false);
                }else {
                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                    Config.cacheNight(getContext(), true);
                }
                //切换夜间模式后刷新状态栏字体颜色
                EventBus.getDefault().post(new RefreshStatusBarEvent());
            }
        });
        mBinding.rlHeader.setOnClickListener(this);
        mBinding.blockStatus.setOnClickListener(this);
        mBinding.blockFriend.setOnClickListener(this);
        mBinding.blockFollower.setOnClickListener(this);
        mBinding.itemFavorite.setOnClickListener(this);
        mBinding.itemTheme.setOnClickListener(this);
        mBinding.itemSettings.setOnClickListener(this);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.switchButton.setChecked(Config.getCachedNight(getContext()));
    }

    private void initData(){
        HttpUtils.getInstance().getUserService()
                .getUserInfo(AccessTokenKeeper.getInstance().getUid(), null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserBean>() {
                    @Override
                    public void onCompleted() {
                        L.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError:"+e.toString());
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
            case R.id.rl_header:
            case R.id.block_status:
                startActivity(new Intent(getActivity(), UserActivity.class)
                        .putExtra(UserActivity.INTENT_KEY_UID, AccessTokenKeeper.getInstance().getUid()));
                break;
            case R.id.block_friend:
                startActivity(new Intent(getActivity(), FriendshipActivity.class)
                        .putExtra(FriendshipActivity.INTENT_TYPE, FriendshipActivity.TYPE_FRIENDS)
                .putExtra(FriendshipActivity.INTENT_UID, AccessTokenKeeper.getInstance().getUid()));
                break;
            case R.id.block_follower:
                startActivity(new Intent(getActivity(), FriendshipActivity.class)
                        .putExtra(FriendshipActivity.INTENT_TYPE, FriendshipActivity.TYPE_FOLLOWERS)
                        .putExtra(FriendshipActivity.INTENT_UID, AccessTokenKeeper.getInstance().getUid()));
                break;
            case R.id.item_favorite:
                startActivity(new Intent(getActivity(), FavoriteActivity.class));
                break;
            case R.id.item_theme:
                startActivity(new Intent(getActivity(), ThemeActivity.class));
                break;
            case R.id.item_settings:
                startActivity(new Intent(getActivity(), SetupActivity.class)
                        .putExtra(SetupActivity.INTENT_SCREENN_NAME, mBinding.tvSreenName.getText().toString()));
                break;
        }

    }
}
