package com.codez.collar.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.AccountAdapter;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.auth.AccessTokenManager;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.Token;
import com.codez.collar.bean.TokenList;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.ActivityAccountBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AccountActivity extends BaseActivity<ActivityAccountBinding> implements View.OnClickListener {

    private AccountAdapter mAdapter;

    @Override
    public int setContent() {
        return R.layout.activity_account;
    }

    @Override
    public void initView() {
        setToolbarTitle(mBinding.toolbar, "账号管理");

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AccountAdapter(this);
        mBinding.recyclerView.setAdapter(mAdapter);

        mBinding.btnLogout.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        loadData();
    }

    private void loadData() {
        if (!AccessTokenKeeper.readAccessToken(this).isSessionsValid()) {
            Intent intent = new Intent(this, WebviewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        AccessTokenManager manager = new AccessTokenManager();
        TokenList tokenList = manager.getTokenList(this);
        final List<Token> lists = tokenList.getTokenList();

        for (Token token : lists) {
            HttpUtils.getInstance().getUserService(this)
                    .getUserInfo(token.getUid(), null)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UserBean>() {
                        @Override
                        public void onCompleted() {
                            L.e("onCompleted");
                        }

                        @Override
                        public void onError(Throwable e) {
                            L.e("onError");
                        }

                        @Override
                        public void onNext(UserBean userBean) {
                            mAdapter.add(userBean);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                AccessTokenKeeper.clear(this);
                loadData();
                break;
        }
    }
}
