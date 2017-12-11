package com.codez.collar.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.DirectMsgConversationAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.DirectMsgConversationResultBean;
import com.codez.collar.databinding.ActivityDirectMsgBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;
import com.codez.collar.utils.T;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DirectMsgActivity extends BaseActivity<ActivityDirectMsgBinding> implements View.OnClickListener{

    public static String INTENT_UID = "uid";
    public static String INTENT_SCREEN_NAME = "screen_name";
    private String mScreenName;
    private String mUid;

    private DirectMsgConversationAdapter mAdapter;


    @Override
    public int setContent() {
        return R.layout.activity_direct_msg;
    }

    @Override
    public void initView() {

        //获取intent传递过来的uid,screen_name
        mUid = getIntent().getStringExtra(INTENT_UID);
        mScreenName = getIntent().getStringExtra(INTENT_SCREEN_NAME);
        setToolbarTitle(mBinding.toolbar,mScreenName);


        mAdapter = new DirectMsgConversationAdapter(this);
        mAdapter.setUid(mUid);
        mBinding.recyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);

        //设置edittext
        mBinding.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(s.toString())) {
                    mBinding.ivCommit.setSelected(false);
                }else{
                    mBinding.ivCommit.setSelected(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mBinding.etContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mBinding.rlAdditional.setVisibility(View.VISIBLE);
                }else{
                    mBinding.rlAdditional.setVisibility(View.GONE);
                }
            }
        });

        mBinding.ivCommit.setOnClickListener(this);

        loadData();
    }

    private void loadData() {
        HttpUtils.getInstance().getDirectMsgService(this)
                .getDirectMsgConversation(mUid,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DirectMsgConversationResultBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        T.s(DirectMsgActivity.this, "数据加载失败");
                        L.e(e.toString());
                    }

                    @Override
                    public void onNext(DirectMsgConversationResultBean directMsgConversationResultBean) {
                        mAdapter.addAll(directMsgConversationResultBean.getDirect_messages());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void createDirectMsg() {
        //简单对微博内容长度进行过滤
        //TODO:完善过滤方法
        if (mBinding.etContent.getText().toString().equals(" ")||mBinding.etContent.getText().toString().equals("\n")) {
            T.s(this,"无效内容");
            return;
        }
        //发送
        T.s(this,"TODO");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_commit:
                if (mBinding.ivCommit.isSelected()) {
                    createDirectMsg();
                }
                break;
        }
    }


}
