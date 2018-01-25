package com.codez.collar.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codez.collar.Config;
import com.codez.collar.MainActivity;
import com.codez.collar.R;
import com.codez.collar.auth.AccessTokenManager;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivityLoginBinding;
import com.codez.collar.utils.L;
import com.codez.collar.utils.RomUtils;

import skin.support.SkinCompatManager;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private String authurl = "https://open.weibo.cn/oauth2/authorize" + "?" + "client_id=" + Config.APP_KEY
            + "&response_type=token&redirect_uri=" + Config.REDIRECT_URL
            + "&key_hash=" + Config.AppSecret + "&packagename=com.eico.weico"
            + "&display=mobile" + "&scope=" + Config.SCOPE;

    private boolean isComeFromAccountAty;//是否由账号页面跳转而来

    @Override
    public int setContent() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

        isComeFromAccountAty = getIntent().getBooleanExtra("comFromAccountActivity", false);
        if (!isComeFromAccountAty){
            setSwipeBackEnable(false);
        }

        //若当前手机系统无法修改状态栏黑色字体，则不选用默认白色主题
        if (Config.getCachedTheme(this).equals("a") && !Config.getCachedNight(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            } else if (RomUtils.isMIUI() && MIUISetStatusBarLightMode(getWindow(), true)) {

            } else if (RomUtils.isFlyme() && FlymeSetStatusBarLightMode(getWindow(), true)) {

            } else {
                SkinCompatManager.getInstance().loadSkin("b", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                Config.cacheTheme(this, "b");
            }
        }


        final WebSettings webSettings = mBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mBinding.webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.e("loading url:"+url);
                if (url.startsWith(Config.REDIRECT_URL)) {
                    view.stopLoading();
                    handleRedirectedUrl(url);
                }else{
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                L.e("page url:"+url);
                if (!url.equals("about:blank") && url.startsWith(Config.REDIRECT_URL)) {
                    view.stopLoading();
                    handleRedirectedUrl(url);
                    return;
                }
                super.onPageStarted(view, url, favicon);
            }
        });
        mBinding.webView.loadUrl(authurl);
        mBinding.webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mBinding.webView.canGoBack()) {
                            mBinding.webView.goBack();
                        }else {
                            finish();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void handleRedirectedUrl(String url) {
        if (!url.contains("error")) {
            int accessTokenIndex = url.indexOf("access_token=");
            int expiresInIndex = url.indexOf("expires_in=");
            int refreshTokenIndex = url.indexOf("refresh_token=");
            int uidIndex = url.indexOf("uid=");

            String accessToken = url.substring(accessTokenIndex + 13,
                    url.indexOf("&", accessTokenIndex));
            String expiresIn = url.substring(expiresInIndex + 11,
                    url.indexOf("&", expiresInIndex));
            String refreshToken = url.substring(refreshTokenIndex + 14,
                    url.indexOf("&", refreshTokenIndex));
            String uid = new String();
            if (url.contains("scope=")) {
                uid = url.substring(uidIndex + 4,
                        url.indexOf("&", uidIndex));
            }else{
                uid = url.substring(uidIndex + 4);
            }

            AccessTokenManager accessTokenManager = new AccessTokenManager();
            accessTokenManager.addToken(this, accessToken, expiresIn, refreshToken, uid);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtra("fisrtstart", true);
            if (isComeFromAccountAty) {
                intent.putExtra("comeFromAccoutActivity", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //账户信息需保存到本地，需获取本地sd卡读取权限
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
