package com.codez.collar.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.codez.collar.Config;

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

        setStatusBarTranslucent();
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

}
