package com.codez.collar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.activity.UserActivity;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.bean.WeiboBean;
import com.codez.collar.databinding.FragmentHomeBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.ui.HomeTitleTextView;
import com.codez.collar.utils.L;
import com.codez.collar.utils.T;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener {

    private boolean isLeft;
    @Override
    public int setContent() {
        return R.layout.fragment_home;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void initView(View root) {
        isLeft = true;
        mBinding.btnAdd.addElement(R.drawable.ic_add_text, R.color.colorHighlight, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.s(getContext(), "add text");
            }
        });
        mBinding.btnAdd.addElement(R.drawable.ic_add_album, R.color.colorHighlight, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.s(getContext(), "add album");
            }
        });
        mBinding.btnAdd.addElement(R.drawable.ic_add_camera, R.color.colorHighlight, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.s(getContext(), "add camera");
            }
        });
        mBinding.btnAdd.setAngle(90);
        mBinding.btnAdd.setmScale(0.8f);
        mBinding.btnAdd.setLength(250);

        mBinding.tvLeft.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
        mBinding.tvRight.changeState(HomeTitleTextView.STATE_UNSELECTED);
        mBinding.tvLeft.setOnClickListener(this);
        mBinding.tvRight.setOnClickListener(this);
        mBinding.ivScan.setOnClickListener(this);
        mBinding.ivUser.setOnClickListener(this);
        initData();
    }

    private void initData(){

//        Subscription get =
                HttpUtils.getInstance().getWeiboService(getContext())
                .getWeibo("5538639136",1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeiboBean>() {
                    @Override
                    public void onCompleted() {
                        L.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError:"+e.toString());
                    }

                    @Override
                    public void onNext(WeiboBean weiboBean) {
                        L.e("onNext:"+weiboBean.getMax_id());
                        List<StatusBean> list = weiboBean.getStatuses();
                        L.e("size:" + list.size());
                        L.e("no.1:"+list.get(0).toString());
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                if (isLeft){
                    if (mBinding.tvLeft.getState() == HomeTitleTextView.STATE_SELECTED_CLOSE) {
                        mBinding.tvLeft.changeState(HomeTitleTextView.STATE_SELECTED_OPEN);
                        //TODO:popupWindow open
                        T.s(getContext(),"left open");
                    }else{
                        mBinding.tvLeft.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
                        //TODO:popWindow close
                        T.s(getContext(),"left close");
                    }
                } else {
                    mBinding.tvLeft.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
                    mBinding.tvRight.changeState(HomeTitleTextView.STATE_UNSELECTED);
                    isLeft = true;
                }
                break;
            case R.id.tv_right:
                if (isLeft){
                    mBinding.tvLeft.changeState(HomeTitleTextView.STATE_UNSELECTED);
                    mBinding.tvRight.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
                    isLeft = false;
                }else{
                    if (mBinding.tvRight.getState() == HomeTitleTextView.STATE_SELECTED_CLOSE) {
                        mBinding.tvRight.changeState(HomeTitleTextView.STATE_SELECTED_OPEN);
                        //TODO:popupWindow open
                        T.s(getContext(),"right open");
                    }else{
                        mBinding.tvRight.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
                        //TODO:popWindow close
                        T.s(getContext(),"right close");
                    }
                }
                break;
            case R.id.iv_user:
                startActivity(new Intent(getActivity(), UserActivity.class)
                        .putExtra("uid", AccessTokenKeeper.getUid(getContext())));
                break;
            case R.id.iv_scan:
                break;
        }

    }
}
