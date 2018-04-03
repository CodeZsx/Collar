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
import com.codez.collar.bean.UpgradeInfoBean;
import com.codez.collar.databinding.ActivitySetupBinding;
import com.codez.collar.databinding.DialogNewVersionBinding;
import com.codez.collar.event.NewAppVersionEvent;
import com.codez.collar.event.ToastEvent;
import com.codez.collar.manager.UpgradeManager;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SetupActivity extends BaseActivity<ActivitySetupBinding> implements View.OnClickListener{

    private static final String TAG = "SetupActivity";
    public static final String INTENT_SCREENN_NAME = "screen_name";

    private AlertDialog mVersionDialog;

    @Override
    public int setContent() {
        return R.layout.activity_setup;
    }

    @Override
    public void initView() {
        EventBusUtils.register(this);
        setToolbarTitle(mBinding.toolbar, "设置");

        mBinding.tvHint.setText(getIntent().getStringExtra(INTENT_SCREENN_NAME));

        mBinding.rlAccount.setOnClickListener(this);
        mBinding.rlUpgrade.setOnClickListener(this);


    }


    private void showVersionDialog(UpgradeInfoBean info) {
        mVersionDialog = new AlertDialog.Builder(this).create();
        mVersionDialog.show();
        mVersionDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mVersionDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);

        Window window = mVersionDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DialogNewVersionBinding dialogBinding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.dialog_new_version, null, false);
        dialogBinding.tvTitle.setText("发现新版本：v" + info.getVersion_name());
        dialogBinding.tvInfo.setText("文件大小：");
        dialogBinding.tvContent.setText(info.getDescription());

        View contentView = dialogBinding.getRoot();
//        contentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        window.setContentView(contentView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_account:
                startActivity(new Intent(this, AccountActivity.class));
                break;

            case R.id.rl_upgrade:
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
        Log.i(TAG, "onNewAppVersionEvent:" + event.getInfo().toString());
        //TODO:load local file or download from server
    }

    @Override
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        super.onDestroy();
    }
}
