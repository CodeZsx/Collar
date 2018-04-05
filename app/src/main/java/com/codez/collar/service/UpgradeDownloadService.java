package com.codez.collar.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.format.Formatter;
import android.util.Log;

import com.codez.collar.R;
import com.codez.collar.bean.UpgradeInfoBean;
import com.codez.collar.manager.UpgradeManager;
import com.codez.collar.utils.Constants;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;


/**
 * Created by codez on 2018/4/3.
 * Description:
 */

public class UpgradeDownloadService extends Service{
    private static final String TAG = "UpgradeDownloadService";
    public static final String INTENT_ACTION = "action";
    public static final String INTENT_UPGRADE_INFO = "upgrade_info";
    private static final int ACTION_PAUSE = 1;
    private static final int ACTION_CONTINUE = 2;
    private static final int ACTION_CANCEL = 3;

    private int downloadId;
    private int progress = 1;
    private String path;
    private UpgradeInfoBean upgradeInfoBean;


    //下载中取消
    private boolean isPause = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }
        int action = intent.getIntExtra(INTENT_ACTION, 0);
        if (action == ACTION_PAUSE) {
            FileDownloader.getImpl().pauseAll();
            return START_NOT_STICKY;
        } else if (action == ACTION_CANCEL) {
            //如果当前是暂停状态，则可以直接删除
            if (isPause) {
                FileDownloader.getImpl().clear(downloadId, path);
                stopForeground(true);
            }else{
                //否则，先暂停再移除
                FileDownloader.getImpl().pauseAll();
                isPause = true;
            }
            return START_NOT_STICKY;
        } else if (action == ACTION_CONTINUE) {
            //go on
        }
        Log.i(TAG, "action:" + action);
        if (upgradeInfoBean == null) {
            upgradeInfoBean = (UpgradeInfoBean) intent.getSerializableExtra(INTENT_UPGRADE_INFO);
            if (upgradeInfoBean == null) {
                return START_NOT_STICKY;
            }
        }
        path = UpgradeManager.getInstance().getApkPath(upgradeInfoBean.getVersion_name());
        Log.i(TAG, "save apk to path:" + path);
        downloadId = FileDownloader.getImpl()
                .create(upgradeInfoBean.getFirmware_url())
                .setPath(path)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i(TAG, "FileDownloader pending");
                        isPause = false;
                        updateNotification(task, soFarBytes, totalBytes, ACTION_CONTINUE);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        isPause = false;
                        updateNotification(task, soFarBytes, totalBytes, ACTION_CONTINUE);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.i(TAG, "FileDownloader completed");
                        isPause = false;
                        startNotification(100, "下载完成", 0, ACTION_CONTINUE);
                        UpgradeManager.getInstance().installApk(UpgradeDownloadService.this, path);
                        stopForeground(true);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i(TAG, "FileDownloader paused");
                        if (!isPause) {
                            isPause = true;
                            updateNotification(task, soFarBytes, totalBytes, ACTION_PAUSE);
                        } else {
                            FileDownloader.getImpl().clear(downloadId, path);
                            stopForeground(true);
                        }
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.i(TAG, "FileDownloader error:" + e.toString());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.i(TAG, "FileDownloader warn");
                    }
                })
                .setWifiRequired(false)
                .start();
        return START_NOT_STICKY;
    }

    /**
     * 处理通知更新内容
     * @param task
     * @param soFarBytes
     * @param totalBytes
     * @param action
     */
    private void updateNotification(BaseDownloadTask task, int soFarBytes, int totalBytes, int action) {
        Log.d(TAG, "progress:" + progress);
        progress = (int) (((float) soFarBytes / totalBytes) * 100);
        String fileSize = Formatter.formatFileSize(this, soFarBytes)
                + "/ " + Formatter.formatFileSize(this, totalBytes);
        startNotification(progress, fileSize, task.getSpeed(), action);
    }

    /**
     * 更新通知
     * @param progress
     * @param fileSize
     * @param speed
     * @param action
     */
    private void startNotification(int progress, String fileSize, int speed, int action) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(UpgradeDownloadService.this)
                .setContentTitle("正在下载("+progress+"%)")
                .setSmallIcon(R.mipmap.logo_collar)
                .setProgress(100, progress,false)
                .setContentText(fileSize);

//        RemoteViews remoteViews = new RemoteViews(BaseApp.getAppPackageName(), R.layout.layout_download_apk);
//        remoteViews.setTextViewText(R.id.tv_download_filesize, fileSize);
//        if (action == ACTION_PAUSE) {
//            remoteViews.setTextViewText(R.id.btn_download_apk_pause, "继续");
//            action = ACTION_CONTINUE;
//        } else {
//            remoteViews.setTextViewText(R.id.btn_download_apk_pause, "暂停");
//            action = ACTION_PAUSE;
//        }
//        remoteViews.setTextViewText(R.id.tv_download_speed, speed + "KB/s");
//        remoteViews.setTextViewText(R.id.tv_download_progress, progress + "%");
//        remoteViews.setProgressBar(R.id.progressBar_download, 100, progress, false);
//
//        Intent pauseStartIntent = new Intent(this, UpgradeDownloadService.class);
//        pauseStartIntent.putExtra(INTENT_ACTION, action);
//        PendingIntent pauseStartPendingIntent = PendingIntent.getService(this,
//                action, pauseStartIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.btn_download_apk_pause, pauseStartPendingIntent);
//
//        Intent cancelIntent = new Intent(this, UpgradeDownloadService.class);
//        cancelIntent.putExtra(INTENT_ACTION, ACTION_CANCEL);
//        PendingIntent cancelPendingIntent = PendingIntent.getService(this,
//                ACTION_CANCEL, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.btn_download_apk_cancel, cancelPendingIntent);
//
//        builder.setContent(remoteViews);


//        startForeground(Constants.APK_DOWNLOAD_NOTIFICATION_ID, notification);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(Constants.APK_DOWNLOAD_NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }
}
