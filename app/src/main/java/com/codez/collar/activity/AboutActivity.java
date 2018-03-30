package com.codez.collar.activity;

import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.base.BaseApp;
import com.codez.collar.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseActivity<ActivityAboutBinding> implements View.OnClickListener{

    private static final String TAG = "AboutActivty";

    @Override
    public int setContent() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        setToolbarTitle(mBinding.toolbar, "关于");

        String postFix = "";
        if (BaseApp.isAppDebug()){
            postFix = "-D";
        }
        String versionName = BaseApp.getAppVersionName();
        int versionCode = BaseApp.getAppVersionCode();
        mBinding.tvVersion.setText("v" + versionName + "-" + versionCode + postFix);
        Spanned text = Html.fromHtml("<a href=\"http://blog.csdn.net/qq_28484355\" style=\"color:#E22930\">萌鼠喝酸奶的博客</a>");
        mBinding.tvBlog.setText(text);
        mBinding.tvBlog.setOnClickListener(this);
        Spanned textGithub = Html.fromHtml("<a href=\"https://github.com/CodeZsx\" style=\"color:#E22930\">GitHub</a>");
        mBinding.tvGithub.setText(textGithub);
        mBinding.tvGithub.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_blog:
                startActivity(new Intent(AboutActivity.this, WebviewActivity.class)
                        .putExtra(WebviewActivity.INTENT_URL, "http://blog.csdn.net/qq_28484355"));
                break;
            case R.id.tv_github:
                startActivity(new Intent(AboutActivity.this, WebviewActivity.class)
                        .putExtra(WebviewActivity.INTENT_URL, "https://github.com/CodeZsx"));
                break;
        }
    }
}
