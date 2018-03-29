package com.codez.collar.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.UserAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.ActivityBaseListBinding;
import com.codez.collar.event.ToastEvent;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.EventBusUtils;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserListActivity extends BaseActivity<ActivityBaseListBinding> implements View.OnClickListener{

    private static final String TAG = "UserListActivity";
    public static final String INTENT_SEARCH_WORD = "search_word";
    private int curPage;
    private String queryWord = null;
    private UserAdapter mAdapter;

    @Override
    public int setContent() {
        return R.layout.activity_base_list;
    }

    @Override
    public void initView() {
        setToolbarTitle(mBinding.toolbar, "相关用户");
        queryWord = getIntent().getStringExtra(INTENT_SEARCH_WORD);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mAdapter = new UserAdapter(this);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.swipeRefreshLayout.setEnabled(false);

        loadData();
    }

    private void loadData() {
        if (queryWord != null) {
            HttpUtils.getInstance().getSearchService()
                    .searchUsers(queryWord, 1, 20)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<UserBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            EventBusUtils.sendEvent(ToastEvent.newToastEvent("数据加载失败"));
                            Log.e(TAG, e.toString());
                        }

                        @Override
                        public void onNext(List<UserBean> list) {
                            mAdapter.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
