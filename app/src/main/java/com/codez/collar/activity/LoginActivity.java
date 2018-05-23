package com.codez.collar.activity;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.codez.collar.base.BaseApp;
import com.codez.collar.bean.TokenResultBean;
import com.codez.collar.databinding.ActivityLoginBinding;
import com.codez.collar.databinding.DialogLoadingBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.service.CoreService;
import com.codez.collar.ui.AppDialog;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {
    private static final String TAG = "LoginActivity";
    public static final String INTENT_COME_FROM = "comeFromAccoutActivity";
    //response_type=token
    private String authUrlByToken = "https://open.weibo.cn/oauth2/authorize?"
            + "client_id=" + Config.APP_KEY
            + "&response_type=token"
            + "&redirect_uri=" + Config.REDIRECT_URL
            + "&key_hash=" + Config.APP_SECRET
//            + "&packagename=com.eico.weico"
            + "&packagename=com.codez.collar"
            + "&display=mobile"
            + "&scope=" + Config.SCOPE;
    //response_type=code
    private String authUrlByCode = "https://api.weibo.com/oauth2/authorize?"
            + "client_id=" + Config.APP_KEY
            + "&response_type=code"
            + "&redirect_uri=" + Config.REDIRECT_URL
            + "&display=mobile"
            + "&scope=" + Config.SCOPE;

    private boolean isComeFromAccountAty;//是否由账号页面跳转而来
    private AppDialog mLoadingDialog;

    @Override
    public int setContent() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

        //状态栏不透明
        setStatusBarUnTranslucent();

        isComeFromAccountAty = getIntent().getBooleanExtra("comFromAccountActivity", false);
        if (!isComeFromAccountAty){
            setSwipeBackEnable(false);
        }

        final WebSettings webSettings = mBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mBinding.webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "loading url:" + url);
                if (url.startsWith(Config.REDIRECT_URL)) {
                    view.stopLoading();
                    handleRedirectedUrlForCode(url);
                }else{
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i(TAG, "page url:"+url);
                if (!url.equals("about:blank") && url.startsWith(Config.REDIRECT_URL)) {
                    view.stopLoading();
                    handleRedirectedUrlForCode(url);
                    return;
                }
                super.onPageStarted(view, url, favicon);
            }
        });
        mBinding.webView.loadUrl(authUrlByCode);
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
//        String result = WeiboSecurityUtils.calculateSInJava(this, "1234", "567");
//        Log.i(TAG, "result:" + result);
    }

    //response_type=token,直接返回token
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
                intent.putExtra(INTENT_COME_FROM, true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            startActivity(intent);
            finish();
        }else{
            //置入固定session
            String accessToken = "2.00SDZpCG06XASO93f07e6842I6NGmC";
            String expiresIn = "4668503041464";
            String refreshToken = "2.00SDZpCG06XASObf188f5bfakyiyZC";
            String uid = "5538639136";

            AccessTokenManager accessTokenManager = new AccessTokenManager();
            accessTokenManager.addToken(this, accessToken, expiresIn, refreshToken, uid);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            if (isComeFromAccountAty) {
                intent.putExtra(INTENT_COME_FROM, true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            startActivity(intent);
            finish();
        }
    }
    //response_type=code，返回code，用code去请求token
    private void handleRedirectedUrlForCode(String url) {
        if (!url.contains("error")) {
            showLoadingDialog();
            int codeIndex = url.indexOf("code=");
            String code = url.substring(codeIndex + 5, url.length());
            String tokenUrl = "https://api.weibo.com/oauth2/access_token?"
                    + "grant_type=authorization_code"
                    + "&client_id="+Config.APP_KEY
                    + "&client_secret="+Config.APP_SECRET
                    + "&redirect_uri="+Config.REDIRECT_URL
                    + "&code="+code;

            HttpUtils.getInstance().getTokenService()
                    .getAccessToken(tokenUrl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TokenResultBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError:" + e.toString());
                        }

                        @Override
                        public void onNext(TokenResultBean bean) {
                            Log.i(TAG, "onNext:get token success");
                            if ("5538639136".equals(bean.getUid())) {
                                saveDefaultAccount();
                                return;
                            }
                            AccessTokenManager accessTokenManager = new AccessTokenManager();
                            accessTokenManager.addToken(LoginActivity.this, bean.getAccess_token(), bean.getExpires_in(), null, bean.getUid());
                            hideLoadingDialog();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            if (isComeFromAccountAty) {
                                intent.putExtra(INTENT_COME_FROM, true);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            }
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    });
        }else{
            saveDefaultAccount();
        }
    }
    private void saveDefaultAccount() {
        //置入固定session
        String accessToken = "2.00SDZpCG06XASO93f07e6842I6NGmC";
        String expiresIn = "4668503041464";
        String refreshToken = "2.00SDZpCG06XASObf188f5bfakyiyZC";
        String uid = "5538639136";

        AccessTokenManager accessTokenManager = new AccessTokenManager();
        accessTokenManager.addToken(this, accessToken, expiresIn, refreshToken, uid);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        if (!isComeFromAccountAty) {
            startActivity(intent);
            finish();
        }else{
            intent.putExtra(INTENT_COME_FROM, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //重启service
            startService(new Intent(this, CoreService.class));
            //重启应用
            intent = BaseApp.sContext.getPackageManager()
                    .getLaunchIntentForPackage(BaseApp.getAppPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            BaseApp.sContext.startActivity(intent);
        }
    }

    private void showLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        DialogLoadingBinding dialogBinding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.dialog_loading, null, false);
        mLoadingDialog = new AppDialog(this);
        mLoadingDialog.setView(dialogBinding.getRoot())
                .show();
    }
    private void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //账户信息需保存到本地，需获取本地sd卡读取权限
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
