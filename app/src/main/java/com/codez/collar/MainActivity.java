package com.codez.collar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.codez.collar.activity.PostActivity;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.UpgradeInfoBean;
import com.codez.collar.databinding.ActivityMainBinding;
import com.codez.collar.databinding.DialogNewVersionBinding;
import com.codez.collar.event.NewAppVersionEvent;
import com.codez.collar.event.TranslucentMaskDisplayEvent;
import com.codez.collar.event.UnreadNoticeEvent;
import com.codez.collar.fragment.HomeFragment;
import com.codez.collar.fragment.MineFragment;
import com.codez.collar.fragment.MsgFragment;
import com.codez.collar.manager.UpgradeManager;
import com.codez.collar.service.UpgradeDownloadService;
import com.codez.collar.ui.AppDialog;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.TimeUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private Fragment fragments[];
    private int curIndex;

    @Override
    public int setContent() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

        Log.i(TAG, AccessTokenKeeper.getInstance().readAccessToken().toString());
        EventBusUtils.register(this);
        //MainActivity不需要滑动后退
        setSwipeBackEnable(false);
        fragments = new Fragment[]{new HomeFragment(), new MsgFragment(), new MineFragment()};
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment: fragments){
            trx.add(R.id.container,fragment).hide(fragment);
        }
        trx.show(fragments[0]).commit();
        mBinding.navBtnHome.setSelected(true);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBinding.translucentMask.getLayoutParams();
        int marginTop = (int) (getResources().getDimension(R.dimen.statusbar_height) + getResources().getDimension(R.dimen.toolbar_height));
        lp.setMargins(0,marginTop,0,0);
        mBinding.translucentMask.setLayoutParams(lp);

        mBinding.menuFloating.setVisibility(View.VISIBLE);
        mBinding.menuFloating.setClosedOnTouchOutside(true);

        mBinding.rlHome.setOnClickListener(this);
        mBinding.rlMsg.setOnClickListener(this);
        mBinding.rlMine.setOnClickListener(this);
        mBinding.btnPostText.setOnClickListener(this);
        mBinding.btnPostImage.setOnClickListener(this);

        UpgradeManager.getInstance().startCheckUpgradeTask(false, null);


    }

    private void changeFragment(int index){
        if (index==curIndex) return;

        FragmentTransaction trx = getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        trx.hide(fragments[curIndex]);
        if (!fragments[index].isAdded()) {
            trx.add(R.id.container, fragments[index]);
        }
        trx.show(fragments[index]).commit();
        if (index == 0) {
            TranslateAnimation showAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            showAnim.setDuration(500);
            mBinding.menuFloating.startAnimation(showAnim);
            mBinding.menuFloating.setVisibility(View.VISIBLE);
        } else if (curIndex == 0) {
            TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            hideAnim.setDuration(500);
            mBinding.menuFloating.startAnimation(hideAnim);
            mBinding.menuFloating.setVisibility(View.GONE);
        }
        curIndex = index;
    }
    private void showVersionDialog(final UpgradeInfoBean info) {
        final AppDialog verDialog = new AppDialog(this);
        DialogNewVersionBinding dialogBinding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.dialog_new_version, null, false);
        dialogBinding.tvTitle.setText("发现新版本：v" + info.getVersion_name());
        dialogBinding.tvInfo.setText("文件信息：" + info.getFile_size()+" [ "+ TimeUtil.getYMDXie(info.getRelease_time())+" ]");
        dialogBinding.tvContent.setText(info.getDescription());
        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verDialog.dismiss();
            }
        });
        dialogBinding.btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verDialog.dismiss();
                String path = UpgradeManager.getInstance().getApkPath(info.getVersion_name());
                File file = new File(path);
                //安装包存在，直接安装
                if (file.exists()) {
                    Log.i(TAG, "the apk is exists");
                    UpgradeManager.getInstance().installApk(MainActivity.this, path);
                }else{
                    //启动apk下载服务
                    startService(new Intent(MainActivity.this, UpgradeDownloadService.class)
                            .putExtra(UpgradeDownloadService.INTENT_UPGRADE_INFO, info));
                }
            }
        });
        verDialog.setView(dialogBinding.getRoot())
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home:
                mBinding.navBtnHome.setSelected(true);
                mBinding.navBtnMsg.setSelected(false);
                mBinding.navBtnMine.setSelected(false);
                changeFragment(0);
                break;
            case R.id.rl_msg:
                mBinding.navBtnHome.setSelected(false);
                mBinding.navBtnMsg.setSelected(true);
                mBinding.navBtnMine.setSelected(false);
                changeFragment(1);
                break;
            case R.id.rl_mine:
                mBinding.navBtnHome.setSelected(false);
                mBinding.navBtnMsg.setSelected(false);
                mBinding.navBtnMine.setSelected(true);
                changeFragment(2);
                break;
            case R.id.btn_post_text:
                startActivity(new Intent(this, PostActivity.class));
                break;
            case R.id.btn_post_image:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnreadNoticeEvent event) {
        int count = event.getCount();
        switch (event.getNavigationId()) {
            case 1:
                if (count == 0 && mBinding.tvNoticeHome.getVisibility() == View.VISIBLE) {
                    mBinding.tvNoticeHome.setVisibility(View.GONE);
                    mBinding.tvNoticeHome.setText(count + "");
                } else {
                    mBinding.tvNoticeHome.setVisibility(View.VISIBLE);
                    mBinding.tvNoticeHome.setText(count + "");
                }
                break;
            case 2:
                if (count == 0 && mBinding.tvNoticeMsg.getVisibility() == View.VISIBLE) {
                    mBinding.tvNoticeMsg.setVisibility(View.GONE);
                    mBinding.tvNoticeMsg.setText(count + "");
                } else {
                    mBinding.tvNoticeMsg.setVisibility(View.VISIBLE);
                    mBinding.tvNoticeMsg.setText(count + "");
                }
                break;
            case 3:
                if (count == 0 && mBinding.tvNoticeMine.getVisibility() == View.VISIBLE) {
                    mBinding.tvNoticeMine.setVisibility(View.GONE);
                    mBinding.tvNoticeMine.setText(count + "");
                } else {
                    mBinding.tvNoticeMine.setVisibility(View.VISIBLE);
                    mBinding.tvNoticeMine.setText(count + "");
                }
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTranslucentMaskDisplayEvent(TranslucentMaskDisplayEvent event) {
        Log.i(TAG, "onTranslucentMaskDisplayEvent:" + event.isDisplay());
        mBinding.translucentMask.setVisibility(event.isDisplay() ? View.VISIBLE : View.GONE);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewAppVersionEvent(NewAppVersionEvent event) {
        showVersionDialog(event.getInfo());
    }

    @Override
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mBinding.menuFloating.isOpened()) {
            mBinding.menuFloating.close(true);
        } else {
            moveTaskToBack(true);
        }
    }
}
