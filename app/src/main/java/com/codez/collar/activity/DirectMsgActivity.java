package com.codez.collar.activity;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;

import com.codez.collar.R;
import com.codez.collar.adapter.DirectMsgConversationAdapter;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.DirectMsgBean;
import com.codez.collar.bean.DirectMsgConversationResultBean;
import com.codez.collar.databinding.ActivityDirectMsgBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;
import com.codez.collar.utils.T;
import com.sqk.emojirelease.FaceFragment;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);

        FaceFragment faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction().add(R.id.rl_additional, faceFragment).commit();
//        faceFragment.OnEmojiClickListener(new FaceFragment.OnEmojiClickListener(){
//
//            @Override
//            public void onEmojiDelete() {
//
//            }
//
//            @Override
//            public void onEmojiClick(Emoji emoji) {
//                L.e(""+emoji.getContent());
//            }
//        });

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

        mBinding.ivCommit.setOnClickListener(this);

        loadData();
//        mBinding.getRoot().getViewTreeObserver()
//                .addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        controlKeyboardLayout(mBinding.getRoot(), mBinding.recyclerView);
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
        HttpUtils.getInstance().getDirectMsgService(this)
                .createDirectMsg(AccessTokenKeeper.getAccessToken(this),mBinding.etContent.getText().toString(),mUid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DirectMsgBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError:"+e.toString());
                    }

                    @Override
                    public void onNext(DirectMsgBean directMsgBean) {
                        mBinding.etContent.setText("");
                        mAdapter.addToFirst(directMsgBean);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
    private void controlKeyboardLayout(final View root, final View needToScrollView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private Rect r = new Rect();

            @Override
            public void onGlobalLayout() {
                //获取当前界面可视部分
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
                needToScrollView.scrollTo(0, heightDifference);
            }
        });
    }


    private boolean mBackEnable = false;
    private boolean mIsBtnBack = false;
    private int rootBottom = Integer.MIN_VALUE;
//    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new OnGlobalLayoutListener() {
//        @Override
//        public void onGlobalLayout() {
//            Rect r = new Rect();
//            mSearchLayout.getGlobalVisibleRect(r);
//            // 进入Activity时会布局，第一次调用onGlobalLayout，先记录开始软键盘没有弹出时底部的位置
//            if (rootBottom == Integer.MIN_VALUE) {
//                rootBottom = r.bottom;
//                return;
//            }
//            // adjustResize，软键盘弹出后高度会变小
//            if (r.bottom < rootBottom) {
//                mBackEnable = false;
//            } else {
//                mBackEnable = true;
//                if (mIsBtnBack) {
//                    finish();
//                }
//            }
//        }
//    };

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
