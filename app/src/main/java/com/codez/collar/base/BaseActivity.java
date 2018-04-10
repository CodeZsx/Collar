package com.codez.collar.base;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.codez.collar.Config;
import com.codez.collar.R;
import com.codez.collar.base.swipeback.SwipeBackActivityHelper;
import com.codez.collar.event.RefreshStatusBarEvent;
import com.codez.collar.utils.PermissionUtil;
import com.codez.collar.utils.RomUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/17.
 * Description:
 */

public abstract class BaseActivity<VD extends ViewDataBinding> extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    protected VD mBinding;
    private SwipeBackActivityHelper mHelper;

    protected Bundle savedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        mBinding = DataBindingUtil.setContentView(this, setContent());

        setStatusBarTranslucent();
        initView();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null) {
            return mHelper.findViewById(id);
        }
        return v;
    }

    public void setSwipeBackEnable(boolean enable) {
        mHelper.setSwipeBackEnable(enable);
    }

    public abstract int setContent();

    public abstract void initView();

    protected void setToolbar(String title) {

    }

    private void initStatusBar() {
        //仅当当前主题色为白色时，修改状态栏字体颜色
        if (Config.getCachedTheme(this).equals("a") && !Config.getCachedNight(this)) {
            //api23以上，调用系统方法
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            } else if (RomUtils.isMIUI() && MIUISetStatusBarLightMode(getWindow(), true)) {

            } else if (RomUtils.isFlyme() && FlymeSetStatusBarLightMode(getWindow(), true)) {

            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                //之所以和>23的情况分开写，是为了解决：api23时的miui，无法使用SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            }
        }else{
            //api23以上，调用系统方法
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//设置状态栏白色字体
            } else if (RomUtils.isMIUI() && MIUISetStatusBarLightMode(getWindow(), false)) {

            } else if (RomUtils.isFlyme() && FlymeSetStatusBarLightMode(getWindow(), false)) {

            }
        }


    }

    protected void setStatusBarTranslucent() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    protected void setStatusBarUnTranslucent() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    protected void setToolbarTitle(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            actionBar.setTitle(title);
        }
    }
    protected void setBgAlpha(float bgAlpha){
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = bgAlpha;
        getWindow().setAttributes(params);
    }
    protected boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight*2/3 > rect.bottom;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initStatusBar();
    }


    protected void requestPermission(String... permissons) {
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
        if (requestCode == PERPERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermission();
            }
            return;
        }
    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    protected boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 是否为亮色状态栏，即是否设置状态栏字体图标为深色，需要MIUIV6以上
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field  field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){
                Log.e(TAG, "MIUISetStatusBarLightMode:"+e.toString());
            }
        }
        return result;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshStatusBarEvent(RefreshStatusBarEvent event) {
        initStatusBar();
    }
}
