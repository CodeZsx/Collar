package com.codez.collar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivityMainBinding;
import com.codez.collar.fragment.HomeFragment;
import com.codez.collar.fragment.MineFragment;
import com.codez.collar.fragment.MsgFragment;
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

        fragments = new Fragment[]{new HomeFragment(), new MsgFragment(), new MineFragment()};
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragments[0])
                .show(fragments[0]).commit();
        mBinding.navBtnHome.setSelected(true);

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

    public void setNavgationNotice(int index, int count) {
        switch (index) {
            case 1:
                if (count == 0 && mBinding.tvNoticeHome.getVisibility() == View.VISIBLE) {
                    mBinding.tvNoticeHome.setVisibility(View.GONE);
                    mBinding.tvNoticeHome.setText(count + "");
                } else {
                    mBinding.tvNoticeHome.setVisibility(View.VISIBLE);
                    mBinding.tvNoticeHome.setText(count + "");
                }
                break;
            case 2:
                if (count == 0 && mBinding.tvNoticeMsg.getVisibility() == View.VISIBLE) {
                    mBinding.tvNoticeMsg.setVisibility(View.GONE);
                    mBinding.tvNoticeMsg.setText(count + "");
                } else {
                    mBinding.tvNoticeMsg.setVisibility(View.VISIBLE);
                    mBinding.tvNoticeMsg.setText(count + "");
                }
                break;
            case 3:
                if (count == 0 && mBinding.tvNoticeUser.getVisibility() == View.VISIBLE) {
                    mBinding.tvNoticeUser.setVisibility(View.GONE);
                    mBinding.tvNoticeUser.setText(count + "");
                } else {
                    mBinding.tvNoticeUser.setVisibility(View.VISIBLE);
                    mBinding.tvNoticeUser.setText(count + "");
                }
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
