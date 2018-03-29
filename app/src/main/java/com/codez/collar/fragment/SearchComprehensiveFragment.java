package com.codez.collar.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.activity.UserListActivity;
import com.codez.collar.adapter.StatusAdapter;
import com.codez.collar.adapter.UserBlockAdapter;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.StatusResultBean;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.FragmentComprehensiveSearchBinding;
import com.codez.collar.event.SearchWordChangedEvent;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.L;
import com.codez.collar.utils.T;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SearchComprehensiveFragment extends BaseFragment<FragmentComprehensiveSearchBinding>{

    private static final String TAG = "SearchCompFragment";
    private UserBlockAdapter mUserAdapter;
    private StatusAdapter mStatusAdapter;
    private int curPage = 1;

    private String queryWord = null;

    @Override
    public int setContent() {
        return R.layout.fragment_comprehensive_search;
    }

    @Override
    public void initView(View root) {
        EventBusUtils.register(this);
        mUserAdapter = new UserBlockAdapter(getContext());
        mStatusAdapter = new StatusAdapter(getContext(), new StatusAdapter.OnChangeAlphaListener() {
            @Override
            public void setAlpha(float alpha) {
                setBgAlpha(alpha);
            }
        });
        mUserAdapter.setOnMoreUserClickListener(onMoreUserClickListener);
        mBinding.rvUser.setAdapter(mUserAdapter);
        mBinding.rvStatus.setAdapter(mStatusAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.rvUser.setLayoutManager(linearLayoutManager);
        mBinding.rvStatus.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rlMoreUsers.setOnClickListener(onMoreUserClickListener);


        loadData();
    }

    private void loadData() {
        if (queryWord != null) {
            HttpUtils.getInstance().getSearchService()
                    .searchUsers(queryWord,1,20)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<UserBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            T.s(getContext(),"数据加载失败");
                            L.e(e.toString());
                        }

                        @Override
                        public void onNext(List<UserBean> list) {
                            mUserAdapter.addAll(list);
                            mUserAdapter.notifyDataSetChanged();
                        }
                    });
            HttpUtils.getInstance().getSearchService()
                    .searchStatuses(queryWord, curPage, 20)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<StatusResultBean>() {
                        @Override
                        public void onCompleted() {
                            L.e("onCompleted");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(TAG, "onError:"+e.toString());
                        }

                        @Override
                        public void onNext(StatusResultBean resultBean) {
                            mStatusAdapter.addAll(resultBean.getStatuses());
                            mStatusAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private View.OnClickListener onMoreUserClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), UserListActivity.class)
                    .putExtra(UserListActivity.INTENT_SEARCH_WORD, queryWord));
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        EventBusUtils.unregister(this);
        super.onDestroy();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupChangedEvent(SearchWordChangedEvent event) {
        if (event == null) {
            return;
        }
        queryWord = event.getSearchWord();
        curPage = 1;
        loadData();
    }

}
