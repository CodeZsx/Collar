package com.codez.collar.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.DirectMsgAdapter;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.DirectMsgResultBean;
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
//        mBinding.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            int lastVisibleItem;
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
//                    T.s(getContext(), "加载更多");
//                    loadData();
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//            }
//        });
//        mBinding.swipeRefreshLayout.setRefreshing(true);
//        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                curPage = 1;
//                loadData();
//            }
//        });

        loadData();
    }

    private void loadData() {
        HttpUtils.getInstance().getDirectMsgService(getContext())
                .getDirectMsg(curPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DirectMsgResultBean>() {
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
                    public void onNext(DirectMsgResultBean directMsgResultBean) {
                        mAdapter.addAll(directMsgResultBean.getDirect_messages());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
    @Override
    public void onClick(View v) {

    }
}
