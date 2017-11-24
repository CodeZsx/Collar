package com.codez.collar;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivityMainBinding;
import com.codez.collar.fragment.HomeFragment;
import com.codez.collar.fragment.MsgFragment;
import com.codez.collar.fragment.MineFragment;
import com.codez.collar.utils.L;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener{

    boolean isNight = false;

    private Fragment fragments[];
    private int curIndex;

    @Override
    public int setContent() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

        L.e("MainActivity:"+ AccessTokenKeeper.readAccessToken(this).toString());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (!Config.getCachedNight(this)&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
        }

        fragments = new Fragment[]{new HomeFragment(), new MsgFragment(), new MineFragment()};
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragments[0])
                .show(fragments[0]).commit();
        mBinding.navBtnHome.setSelected(true);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        changeFragment(0);
                        return true;
                    case R.id.navigation_dashboard:
                        changeFragment(1);
                        return true;

                }
                return false;
            }
        });
//        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.navigation);
//        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
//        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
//        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_nav_home).setActiveColor(R.color.colorPrimary))
//                .addItem(new BottomNavigationItem(R.drawable.ic_nav_msg).setActiveColor(R.color.colorPrimary))
//                .addItem(new BottomNavigationItem(R.drawable.ic_nav_find).setActiveColor(R.color.colorPrimary))
//                .setFirstSelectedPosition(0)
//                .initialise();

        mBinding.navBtnHome.setOnClickListener(this);
        mBinding.navBtnMsg.setOnClickListener(this);
        mBinding.navBtnUser.setOnClickListener(this);


    }
    private void changeFragment(int index){
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.hide(fragments[curIndex]);
        if (!fragments[index].isAdded()) {
            trx.add(R.id.container, fragments[index]);
        }
        trx.show(fragments[index]).commit();
        curIndex = index;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_btn_home:
                mBinding.navBtnHome.setSelected(true);
                mBinding.navBtnMsg.setSelected(false);
                mBinding.navBtnUser.setSelected(false);
                changeFragment(0);
                break;
            case R.id.nav_btn_msg:
                mBinding.navBtnHome.setSelected(false);
                mBinding.navBtnMsg.setSelected(true);
                mBinding.navBtnUser.setSelected(false);
                changeFragment(1);
                break;
            case R.id.nav_btn_user:
                mBinding.navBtnHome.setSelected(false);
                mBinding.navBtnMsg.setSelected(false);
                mBinding.navBtnUser.setSelected(true);
                changeFragment(2);
                break;
        }
    }

    //记录退出时间
    private long exitTime = 0l;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出"+getString(R.string.app_name), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
