package com.codez.collar.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivityWebviewBinding;
import com.codez.collar.utils.T;

public class WebviewActivity extends BaseActivity<ActivityWebviewBinding> {


    public static final String INTENT_URL = "url";
    private String mUrl;
    @Override
    public int setContent() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView() {

        setToolbarTitle(mBinding.toolbar,"网页");


        mUrl = getIntent().getStringExtra(INTENT_URL);

        final WebSettings webSettings = mBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mBinding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mBinding.webView.loadUrl(mUrl);
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
        mBinding.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 1) {
                    //网页加载完成
                    mBinding.progressbar.setVisibility(View.GONE);
                }else{
                    //加载中
                    mBinding.progressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_copy_link) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText(null, mUrl));
            T.s(this,"已复制到剪贴板");
            return true;
        }else if (id == R.id.action_browser) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl)));
            return true;
        } else if (id == R.id.action_refresh) {
            mBinding.webView.loadUrl(mUrl);
            mBinding.progressbar.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }
}
