package com.codez.collar.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codez.collar.Config;
import com.codez.collar.R;
import com.codez.collar.auth.AccessTokenManager;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivityWebviewBinding;
import com.codez.collar.utils.L;
import com.codez.collar.utils.T;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

public class WebviewActivity extends BaseActivity<ActivityWebviewBinding> {

    String authurl = "https://open.weibo.cn/oauth2/authorize" + "?" + "client_id=" + Config.APP_KEY
            + "&response_type=token&redirect_uri=" + Config.REDIRECT_URL
            + "&key_hash=" + Config.AppSecret + "&packagename=com.eico.weico"
            + "&display=mobile" + "&scope=" + Config.SCOPE;


    @Override
    public int setContent() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView() {
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
//                return super.shouldOverrideUrlLoading(view, url);
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
            L.e("access token:" + accessToken);
            L.e("expires in:" + expiresIn);
            L.e("refresh token:" + refreshToken);
            L.e("uid:" + uid);

            AccessTokenManager accessTokenManager = new AccessTokenManager();
            accessTokenManager.addToken(this, accessToken, expiresIn, refreshToken, uid);
        }
    }

    private void requestSDCard() {
        PermissionsUtil.TipInfo info = new PermissionsUtil.TipInfo("注意:", "Collar需要获取存储权限", "拒绝", "打开权限");

        if (!PermissionsUtil.hasPermission(this, Manifest.permission_group.STORAGE)) {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permission) {
                    T.s(WebviewActivity.this, "用户授予了存储权限");
                }

                @Override
                public void permissionDenied(@NonNull String[] permission) {
                    T.s(WebviewActivity.this, "用户拒绝了存储权限");
                    requestSDCard();
                }
            }, new String[]{Manifest.permission_group.STORAGE}, true, info);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestSDCard();
    }
}
