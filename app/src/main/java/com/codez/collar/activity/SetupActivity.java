package com.codez.collar.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.base.BaseApp;
import com.codez.collar.bean.UpgradeInfoBean;
import com.codez.collar.databinding.ActivitySetupBinding;
import com.codez.collar.databinding.DialogLoadingBinding;
import com.codez.collar.databinding.DialogNewVersionBinding;
import com.codez.collar.event.NewAppVersionEvent;
import com.codez.collar.event.ToastEvent;
import com.codez.collar.manager.UpgradeManager;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.service.UpgradeDownloadService;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.TimeUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SetupActivity extends BaseActivity<ActivitySetupBinding> implements View.OnClickListener{

    private static final String TAG = "SetupActivity";
    public static final String INTENT_SCREENN_NAME = "screen_name";

    private AlertDialog mVersionDialog;
    private AlertDialog mLoadingDialog;

    @Override
    public int setContent() {
        return R.layout.activity_setup;
    }

    @Override
    public void initView() {
        EventBusUtils.register(this);
        setToolbarTitle(mBinding.toolbar, "设置");

        mBinding.tvHint.setText(getIntent().getStringExtra(INTENT_SCREENN_NAME));
        mBinding.tvAppVer.setText("v" + BaseApp.getAppVersionName());

        mBinding.rlAccount.setOnClickListener(this);
        mBinding.rlUpgrade.setOnClickListener(this);


    }

    private void showLoadingDialog() {
        if (mVersionDialog != null) {
            mVersionDialog.dismiss();
        }
        mLoadingDialog = new AlertDialog.Builder(this).create();
        mLoadingDialog.show();
        DialogLoadingBinding dialogBinding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.dialog_loading, null, false);
        View contentView = dialogBinding.getRoot();
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);

        Window window = mLoadingDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setContentView(contentView);
    }

    private void showVersionDialog(final UpgradeInfoBean info) {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mVersionDialog = new AlertDialog.Builder(this).create();
        mVersionDialog.show();
        DialogNewVersionBinding dialogBinding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.dialog_new_version, null, false);
        View contentView = dialogBinding.getRoot();
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);

        Window window = mVersionDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setContentView(contentView);

        dialogBinding.tvTitle.setText("发现新版本：v" + info.getVersion_name());
        dialogBinding.tvInfo.setText("文件信息：" + info.getFile_size()+" [ "+ TimeUtil.getYMDXie(info.getRelease_time())+" ]");
        dialogBinding.tvContent.setText(info.getDescription());
        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVersionDialog.dismiss();
            }
        });
        dialogBinding.btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVersionDialog.dismiss();
                String path = UpgradeManager.getInstance().getApkPath(info.getVersion_name());
                File file = new File(path);
                //安装包存在，直接安装
                if (file.exists()) {
                    Log.i(TAG, "the apk is exists");
                    UpgradeManager.getInstance().installApk(SetupActivity.this, path);
                }else{
                    //启动apk下载服务
                    startService(new Intent(SetupActivity.this, UpgradeDownloadService.class)
                            .putExtra(UpgradeDownloadService.INTENT_UPGRADE_INFO, info));
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_account:
                startActivity(new Intent(this, AccountActivity.class));
                break;

            case R.id.rl_upgrade:
                showLoadingDialog();
                HttpUtils.getInstance().getUpgradeService()
                        .getUpgradeInfo()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<UpgradeInfoBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mLoadingDialog != null) {
                                    mLoadingDialog.dismiss();
                                }
                                Log.i(TAG, "onError:" + e.toString());
                                EventBusUtils.sendEvent(ToastEvent.newToastEvent("未知错误，操作失败"));
                            }

                            @Override
                            public void onNext(UpgradeInfoBean bean) {

                                if (bean == null) {
                                    EventBusUtils.sendEvent(ToastEvent.newToastEvent("未知错误，操作失败"));
                                    return;
                                }
                                Log.i(TAG, "info:" + bean.toString());
                                if (bean.getVersion_name().compareTo(UpgradeManager.getInstance().getAppVersionName())<=0) {
                                    EventBusUtils.sendEvent(ToastEvent.newToastEvent("已是最新版本"));
                                    return;
                                } else {
                                    EventBusUtils.sendEvent(new NewAppVersionEvent(bean, NewAppVersionEvent.MANUAL));
                                    return;
                                }
                            }
                        });
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewAppVersionEvent(NewAppVersionEvent event) {
        showVersionDialog(event.getInfo());
        //TODO:load local file or download from server
    }

    @Override
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        super.onDestroy();
    }
}
