package com.codez.collar.base;

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
import com.codez.collar.tools.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

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
        if (!Config.getCachedNight(this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
    }


    protected void requestPermission(String... permissons) {
        ActivityCompat.requestPermissions(this, permissons, PERPERMISSION_REQUEST_CODE);

        List<String> list = new ArrayList<>();
        for (String permission : permissons) {
            if (!(this.getPackageManager().checkPermission(permission, this.getPackageName()) == PackageManager.PERMISSION_GRANTED)) {
                list.add(permission);
            }
        }

        if (list.size()>0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, list.get(0))) {
                //用户曾经拒绝过授予权限，弹出dialog告知用户开启相关权限
                PermissionUtil.requestPermission(this, new PermissionUtil.PermissionListener() {
                    @Override
                    public void permissionGranted(@NonNull String[] permission) {
                    }

                    @Override
                    public void permissionDenied(@NonNull String[] permission) {

                    }
                }, permissons);

            } else {
                ActivityCompat.requestPermissions(this, permissons, PERPERMISSION_REQUEST_CODE);
            }
        }

    }

    private static final int PERPERMISSION_REQUEST_CODE = 1;

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERPERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermission();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
