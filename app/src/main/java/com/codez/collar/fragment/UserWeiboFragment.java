package com.codez.collar.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.StatusAdapter;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.bean.WeiboBean;
import com.codez.collar.databinding.FragmentUserWeiboBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UserWeiboFragment extends BaseFragment<FragmentUserWeiboBinding> implements View.OnClickListener {

    private static final String UID = "uid";
    private String mUid;
    private int curPage;
    private StatusAdapter mStatusAdapter;
    @Override
    public int setContent() {
        return R.layout.fragment_user_weibo;
    }

    public static UserWeiboFragment newInstance(String uid){
        UserWeiboFragment fragment = new UserWeiboFragment();
        Bundle args = new Bundle();
        args.putString(UID, uid);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initView(View root) {
        if (getArguments() != null) {
            mUid = getArguments().getString(UID);
        }
        curPage = 1;

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mStatusAdapter = new StatusAdapter(getContext());
        mBinding.recyclerView.setAdapter(mStatusAdapter);
        loadData();

    }

    private void loadData() {
        HttpUtils.getInstance().getWeiboService(getContext())
                .getUserStatus(mUid, 1)
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
                        if (curPage == 1) {
                            if (weiboBean != null && weiboBean.getStatuses() != null && weiboBean.getStatuses().size() > 0) {
                                if (mStatusAdapter == null) {
                                    mStatusAdapter = new StatusAdapter(getContext());
                                }
                                mStatusAdapter.setList(weiboBean.getStatuses());
                                mStatusAdapter.notifyDataSetChanged();
                                mBinding.recyclerView.setAdapter(mStatusAdapter);
                            }
                        }else {
                            mStatusAdapter.addAll(weiboBean.getStatuses());
                            mStatusAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {

    }
}
