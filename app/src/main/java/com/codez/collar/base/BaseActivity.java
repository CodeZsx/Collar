package com.codez.collar.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.codez.collar.Config;
import com.codez.collar.utils.PermissionUtil;

/**
 * Created by codez on 2017/11/17.
 * Description:
 */

public abstract class BaseActivity<VD extends ViewDataBinding> extends AppCompatActivity {
    protected VD mBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, setContent());

//        setStatusBarTranslucent();
        if (!Config.getCachedNight(this)&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
        }

        initView();
    }


    public abstract int setContent();
    public abstract void initView();

    protected void setToolbar(String title) {

    }

    protected void setStatusBarTranslucent() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    protected void setToolbarTitle(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            actionBar.setTitle(title);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
    }

    protected void requestPermission(){
        //请求权限的相关操作
        if (!PermissionUtil.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //用户曾经拒绝过授予权限，弹出dialog告知用户开启相关权限
                PermissionUtil.requestPermission(this, new PermissionUtil.PermissionListener() {
                    @Override
                    public void permissionGranted(@NonNull String[] permission) {
                    }

                    @Override
                    public void permissionDenied(@NonNull String[] permission) {

                    }
                }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERPERMISSION_STORAGE);            }
        }
    }

    private int PERPERMISSION_STORAGE = 1;
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERPERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }else{
                requestPermission();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
