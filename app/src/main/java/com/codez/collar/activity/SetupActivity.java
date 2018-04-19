package com.codez.collar.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.base.BaseApp;
import com.codez.collar.bean.UpgradeInfoBean;
import com.codez.collar.databinding.ActivitySetupBinding;
import com.codez.collar.databinding.DialogLoadingBinding;
import com.codez.collar.databinding.DialogNewVersionBinding;
import com.codez.collar.event.NewAppVersionEvent;
import com.codez.collar.manager.UpgradeManager;
import com.codez.collar.service.UpgradeDownloadService;
import com.codez.collar.ui.AppDialog;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.TimeUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public class SetupActivity extends BaseActivity<ActivitySetupBinding> implements View.OnClickListener{

    private static final String TAG = "SetupActivity";
    public static final String INTENT_SCREENN_NAME = "screen_name";

    private AppDialog mVersionDialog;
    private AppDialog mLoadingDialog;

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
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        DialogLoadingBinding dialogBinding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.dialog_loading, null, false);
        mLoadingDialog = new AppDialog(this);
        mLoadingDialog.setView(dialogBinding.getRoot())
                .show();
    }

    private void showVersionDialog(final UpgradeInfoBean info) {
        if (mVersionDialog != null) {
            mVersionDialog.dismiss();
        }
        mVersionDialog = new AppDialog(this);
        DialogNewVersionBinding dialogBinding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.dialog_new_version, null, false);
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
        mVersionDialog.setView(dialogBinding.getRoot())
                .show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_account:
                startActivity(new Intent(this, AccountActivity.class));
                break;

            case R.id.rl_upgrade:
                showLoadingDialog();
                UpgradeManager.getInstance().startCheckUpgradeTask(true, mLoadingDialog);
                break;
        }
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
}
