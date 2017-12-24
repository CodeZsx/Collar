package com.codez.collar.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.codez.collar.MainActivity;
import com.codez.collar.R;
import com.codez.collar.adapter.DirectMsgAdapter;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.DirectMsgUserlistBean;
import com.codez.collar.bean.DirectMsgUserlistResultBean;
import com.codez.collar.databinding.FragmentMsgBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;
import com.codez.collar.utils.T;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MsgFragment extends BaseFragment<FragmentMsgBinding> implements View.OnClickListener {

    private DirectMsgAdapter mAdapter;
    private int curPage = 1;


    @Override
    public int setContent() {
        return R.layout.fragment_msg;
    }

    @Override
    public void initView(View root) {
        mAdapter = new DirectMsgAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setNestedScrollingEnabled(false);

        loadData();
    }

    private void loadData() {
        HttpUtils.getInstance().getDirectMsgService(getContext())
                .getDirectMsgUserlist("0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DirectMsgUserlistResultBean>() {
                    @Override
                    public void onCompleted() {
//                        mBinding.swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        T.s(getContext(),"数据加载失败");
                        L.e(e.toString());
                    }

                    @Override
                    public void onNext(DirectMsgUserlistResultBean directMsgUserlistResultBean) {
                        mAdapter.addAll(directMsgUserlistResultBean.getUser_list());
                        countUnreadMsg();
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private int lastCount = 0;
    private void countUnreadMsg() {
        int count = 0;
        for (DirectMsgUserlistBean bean : mAdapter.getList()) {
            count += bean.getUnread_count();
        }
        if (lastCount != 0 || count != 0) {
            ((MainActivity) getActivity()).setNavgationNotice(2, count);
        }
        lastCount = count;
    }

    @Override
    public void onResume() {
        super.onResume();
        countUnreadMsg();
    }

    @Override
    public void onClick(View v) {

    }
}
