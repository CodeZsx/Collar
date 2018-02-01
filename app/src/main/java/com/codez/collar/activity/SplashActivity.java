package com.codez.collar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.codez.collar.MainActivity;
import com.codez.collar.R;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivitySplashBinding;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    private int countdown = 1;
    private static final int SKIP_COUNTDOWN = 1;
    private static boolean isFinished = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setContent() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        hideBottomUIMenu();
        setSwipeBackEnable(false);
        mBinding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFinished = true;
            }
        });
        mHandler.sendEmptyMessage(SKIP_COUNTDOWN);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SKIP_COUNTDOWN:
                    if (countdown < 1||isFinished) {
                        mBinding.btnSkip.setText("跳过"+countdown--+"s");
                        Intent intent = null;
                        if (AccessTokenKeeper.getInstance().readAccessToken().isSessionsValid()) {
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        }else {
                            intent = new Intent(SplashActivity.this, LoginActivity.class);
                        }
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }else {
                        mBinding.btnSkip.setText("跳过"+countdown--+"s");
                        mHandler.sendEmptyMessageDelayed(SKIP_COUNTDOWN,1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 隐藏虚拟按键
     */
    protected void hideBottomUIMenu(){
        //low api
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //new api versions
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
