package com.codez.collar.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codez.collar.Config;
import com.codez.collar.R;
import com.codez.collar.adapter.FriendshipAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.FriendsIdsResultBean;
import com.codez.collar.bean.FriendshipsResultBean;
import com.codez.collar.databinding.ActivityBaseListBinding;
import com.codez.collar.event.NightModeChangedEvent;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.T;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FriendshipActivity extends BaseActivity<ActivityBaseListBinding> implements View.OnClickListener{
    private static final String TAG = "FriendshipActivity";
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
        EventBusUtils.register(this);
        mType = getIntent().getStringExtra(INTENT_TYPE);
        mUid = getIntent().getStringExtra(INTENT_UID);
        setToolbarTitle(mBinding.toolbar, mType);
        Log.i(TAG, "type："+mType);

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
        EventBusUtils.sendEvent(new NightModeChangedEvent(Config.getCachedNight(this)));
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
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError"+e.toString());
                        mBinding.swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(FriendsIdsResultBean friendsIdsResultBean) {
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
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNightModeChanged(NightModeChangedEvent event) {
        if (event.isNight()) {
            mBinding.swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorItemNormal_night);
            mBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorHighlight);
        } else {
            mBinding.swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorItemNormal);
            mBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorHighlight);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
