package com.codez.collar.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by codez on 2017/11/27.
 * Description:
 */

public class PermissionUtil {
    private static final String TAG = "PermissionUtil";
    private static final String defaultTitle = "帮助";
    private static final String defaultContent = "当前应用缺少必要权限。\n \n 请点击 \"设置\"-\"权限\"-打开所需权限。";
    private static final String defaultCancel = "取消";
    private static final String defaultEnsure = "设置";
    public static boolean hasPermission(Context context, String... permissons) {
        if (permissons.length == 0) {
            return false;
        }
        for (String permission : permissons) {
            if (!(context.getPackageManager().checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermission(final Context context, final PermissionListener listener, @NonNull String[] permission) {
        if (listener == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < 23) {
            if (hasPermission(context, permission)) {
                listener.permissionGranted(permission);
            } else {
                listener.permissionDenied(permission);
            }
            Log.i(TAG, "API level : " + Build.VERSION.SDK_INT + "不需要申请动态权限!");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(defaultTitle);
        builder.setMessage(defaultContent);
        builder.setNegativeButton(defaultCancel, new DialogInterface.OnClickListener(){
            @Override public void onClick(DialogInterface dialog, int which) {
                listener.permissionDenied(null);
            }
        });

        builder.setPositiveButton(defaultEnsure, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                goToSettings(context);
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    public static void goToSettings(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT > 8) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(),null));
        }else{
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    public interface PermissionListener {

        /**
         * 通过授权
         * @param permission
         */
        void permissionGranted(@NonNull String[] permission);

        /**
         * 拒绝授权
         * @param permission
         */
        void permissionDenied(@NonNull String[] permission);
    }
}
