package com.codez.collar.manager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.codez.collar.BuildConfig;
import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.base.BaseApp;
import com.codez.collar.bean.UpgradeInfoBean;
import com.codez.collar.databinding.DialogNewVersionBinding;
import com.codez.collar.db.DBConstants;
import com.codez.collar.db.DataBaseHelper;
import com.codez.collar.event.NewAppVersionEvent;
import com.codez.collar.event.ToastEvent;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.service.UpgradeDownloadService;
import com.codez.collar.ui.AppDialog;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.SDCardUtil;
import com.codez.collar.utils.TimeUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;


/**
 * Created by codez on 2018/4/2.
 * Description:
 */

public enum  UpgradeManager {

    mInstance;
    private String mAppVersionName;
    private DataBaseHelper mDBHelper;
    private static final long CHECK_UPGRADE_INTERVAL = 24*60*60*1000;//自动弹出更新dialog的时间间隔
    private static final String TAG = "UpgradeManager";

    public static UpgradeManager getInstance() {
        return mInstance;
    }

    public String getAppVersionName() {
        if (BuildConfig.DEBUG) {
            return "0.0.0.001";
        }
        if (mAppVersionName == null) {
            mAppVersionName = BaseApp.getAppVersionName();
        }
        return mAppVersionName;
    }

    public String getApkPath(String apkPath) {
        return SDCardUtil.getAppCachePath() + "/" + apkPath + ".apk";
    }

    /**
     * 安装apk
     * @param path
     */
    public void installApk(Context context, String path) {
        File file = new File(path);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BaseApp.sContext,
                    BaseApp.getAppPackageName() + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    /**
     * 开启app检查升级的任务
     * @param isActive 是否是主动查询
     * @param pgDialog
     */
    public void startCheckUpgradeTask(final boolean isActive, final AppDialog pgDialog) {
        long checkFrequence = 4 * 60 * 60;
        long delayTime = 0;//暂时不延迟启动
        Observable.interval(delayTime, checkFrequence, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "upgrade task delay start completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (pgDialog != null) {
                            pgDialog.dismiss();
                        }
                        Log.w(TAG, "check task error");
                        Log.w(TAG, "onError:" + ((e == null) ? "onError:" : "onError:" + e.toString()));
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(Long aLong) {
                        //从服务器检查更新信息
                        HttpUtils.getInstance().getUpgradeService()
                                .getUpgradeInfo()
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(new Observer<UpgradeInfoBean>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.i(TAG, "query upgrade info completed");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (pgDialog != null) {
                                            pgDialog.dismiss();
                                        }
                                        Log.w(TAG, "query upgrade info error");
                                        Log.w(TAG, "onError:" + ((e == null) ? "onError:" : "onError:" + e.toString()));
                                        if (isActive) {
                                            EventBusUtils.sendEvent(ToastEvent.newToastEvent("未知错误，操作失败"));
                                        }
                                    }

                                    @Override
                                    public void onNext(UpgradeInfoBean bean) {
                                        if (pgDialog != null) {
                                            pgDialog.dismiss();
                                        }
                                        if (bean == null && isActive) {
                                            EventBusUtils.sendEvent(ToastEvent.newToastEvent("未知错误，操作失败"));
                                            return;
                                        }
                                        bean.setCheck_time(System.currentTimeMillis());
                                        Log.i(TAG, "info:" + bean.toString());
                                        if (bean.getVersion_name().compareTo(UpgradeManager.getInstance().getAppVersionName()) <= 0 && isActive) {
                                            EventBusUtils.sendEvent(ToastEvent.newToastEvent("已是最新版本"));
                                            return;
                                        } else {
                                            //设置页面主动检查更新，显示dialog
                                            if (isActive){
                                                EventBusUtils.sendEvent(new NewAppVersionEvent(bean, NewAppVersionEvent.MANUAL));
                                            }else{
                                                // MainActivity自动检查更新，弹出dialog的情况：
                                                // 1、本地没有保存过
                                                // 2、当前时间距离本地上次check时间满足INTERVAL；
                                                // 3、服务器版本高于上一次保存的版本，则显示dialog；
                                                String latestVerName = bean.getVersion_name();
                                                UpgradeInfoBean localInfo = getLastUpgradeInfoBean();
                                                if (localInfo == null) {
                                                    EventBusUtils.sendEvent(new NewAppVersionEvent(bean, NewAppVersionEvent.MANUAL));
                                                }else if((System.currentTimeMillis() - localInfo.getCheck_time()) > CHECK_UPGRADE_INTERVAL){
                                                    EventBusUtils.sendEvent(new NewAppVersionEvent(bean, NewAppVersionEvent.MANUAL));
                                                } else if (latestVerName != null && latestVerName.compareTo(localInfo.getVersion_name()) > 0) {
                                                    EventBusUtils.sendEvent(new NewAppVersionEvent(bean, NewAppVersionEvent.MANUAL));
                                                }
                                            }
                                            return;
                                        }
                                    }
                                });
                    }
                });
    }
    public AppDialog getNewVersionDialog(final BaseActivity aty, final UpgradeInfoBean info) {
        final AppDialog dialog = new AppDialog(aty);
        DialogNewVersionBinding dialogBinding = DataBindingUtil.inflate(aty.getLayoutInflater(), R.layout.dialog_new_version, null, false);
        dialogBinding.tvTitle.setText("发现新版本：v" + info.getVersion_name());
        dialogBinding.tvInfo.setText("文件信息：" + info.getFile_size()+" [ "+ TimeUtil.getYMDXie(info.getRelease_time())+" ]");
        dialogBinding.tvContent.setText(info.getDescription());
        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //若是cancel，则保存此次更新信息，避免每次启动应用都弹出更新dialog
                UpgradeManager.getInstance().updateUpgradeInfoBeanIntoDB(info);
                dialog.dismiss();
            }
        });
        dialogBinding.btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String path = UpgradeManager.getInstance().getApkPath(info.getVersion_name());
                File file = new File(path);
                //安装包存在，直接安装
                if (file.exists()) {
                    Log.i(TAG, "the apk is exists");
                    UpgradeManager.getInstance().installApk(aty, path);
                }else{
                    //启动apk下载服务
                    aty.startService(new Intent(aty, UpgradeDownloadService.class)
                            .putExtra(UpgradeDownloadService.INTENT_UPGRADE_INFO, info));
                }
            }
        });
        dialog.setView(dialogBinding.getRoot())
                .show();
        return dialog;
    }

    /**
     * 获取本地存储的上一次检查更新的内容
     * @return
     */
    private UpgradeInfoBean getLastUpgradeInfoBean() {
        UpgradeInfoBean bean = getDBHelper().queryForObject(new DataBaseHelper.RowMapper<UpgradeInfoBean>() {
            @Override
            public UpgradeInfoBean mapRow(Cursor cursor, int index) {
                String data = cursor.getString(cursor.getColumnIndex(DBConstants.CONFIG_COLUMN_VALUE));
                if (data == null) {
                    Log.w(TAG, "nothing data(local upgrade info)");
                    return null;
                } else {
                    return UpgradeInfoBean.jsonToBean(data);
                }
            }
        }, "SELECT * FROM " + DBConstants.TABLE_CONFIG + " WHERE " + DBConstants.CONFIG_COLUMN_KEY + "=?",
                new String[]{DBConstants.CONFIG_UPGRADE});
        return bean;
    }

    private void updateUpgradeInfoBeanIntoDB(UpgradeInfoBean bean) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.CONFIG_COLUMN_KEY, DBConstants.CONFIG_UPGRADE);
        values.put(DBConstants.CONFIG_COLUMN_VALUE, UpgradeInfoBean.beanToJson(bean).toJSONString());
        //根据消息是否在数据库中，而选择更新或插入
        if (getDBHelper().isExistsByField(DBConstants.TABLE_CONFIG, DBConstants.CONFIG_COLUMN_KEY, DBConstants.CONFIG_UPGRADE)) {
            getDBHelper().update(DBConstants.TABLE_CONFIG, values, DBConstants.CONFIG_COLUMN_KEY + "=?", new String[]{DBConstants.CONFIG_UPGRADE});
        }else{
            getDBHelper().insert(DBConstants.TABLE_CONFIG, values);
        }
    }
    private DataBaseHelper getDBHelper(){
        if (mDBHelper == null) {
            Log.w(TAG, "dbhelper is null get again");
            mDBHelper = DataBaseHelper.getDataBaseHelper();
        }
        return mDBHelper;
    }
}
