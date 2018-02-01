package com.codez.collar.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.FriendshipAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.FriendsIdsResultBean;
import com.codez.collar.bean.FriendshipsResultBean;
import com.codez.collar.databinding.ActivityBaseListBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;
import com.codez.collar.utils.T;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FriendshipActivity extends BaseActivity<ActivityBaseListBinding> implements View.OnClickListener{

    public static final String INTENT_TYPE = "type";
    public static final String INTENT_UID = "uid";
    public static final String TYPE_FRIENDS = "关注";
    public static final String TYPE_FOLLOWERS = "粉丝";
    private String mType;
    private String mUid;


    private int curPage = 1;
    private FriendshipAdapter mAdapter;

    @Override
    public int setContent() {
        return R.layout.activity_base_list;
    }

    @Override
    public void initView() {
        mType = getIntent().getStringExtra(INTENT_TYPE);
        mUid = getIntent().getStringExtra(INTENT_UID);
        setToolbarTitle(mBinding.toolbar, mType);
        L.e(mType);

        mAdapter = new FriendshipAdapter(this);
        mAdapter.setType(mType);
        mBinding.recyclerView.setAdapter(mAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);

        mBinding.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    T.s(FriendshipActivity.this, "没有更多了");
//                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        //取消下拉刷新
        mBinding.swipeRefreshLayout.setEnabled(false);
        //设置初始时加载提示
        mBinding.swipeRefreshLayout.setRefreshing(true);

        loadData();
    }

    private void loadData() {
        HttpUtils.getInstance().getFriendshipService()
                .getFriendsIds(mUid, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FriendsIdsResultBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError"+e.toString());
                        mBinding.swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(FriendsIdsResultBean friendsIdsResultBean) {
                        L.e(friendsIdsResultBean.toString());
                        mAdapter.setFriendsIds(friendsIdsResultBean.getIds());
                        loadFriendship();
                    }
                });
    }

    private void loadFriendship() {
        if (mType.equals(this.TYPE_FRIENDS)) {
            HttpUtils.getInstance().getFriendshipService()
                    .getFriends(mUid, null, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<FriendshipsResultBean>() {
                        @Override
                        public void onCompleted() {
                            mBinding.swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            T.s(FriendshipActivity.this,"加载失败");
                        }

                        @Override
                        public void onNext(FriendshipsResultBean friendshipsResultBean) {
                            mAdapter.addAll(friendshipsResultBean.getUsers());
                            mAdapter.notifyDataSetChanged();
                        }
                    });
        } else if (mType.equals(TYPE_FOLLOWERS)) {
            HttpUtils.getInstance().getFriendshipService()
                    .getFollowers(mUid, null, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<FriendshipsResultBean>() {
                        @Override
                        public void onCompleted() {
                            mBinding.swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            T.s(FriendshipActivity.this,"加载失败");
                        }

                        @Override
                        public void onNext(FriendshipsResultBean friendshipsResultBean) {
                            mAdapter.addAll(friendshipsResultBean.getUsers());
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
