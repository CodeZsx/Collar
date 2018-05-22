package com.codez.collar.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.codez.collar.R;
import com.codez.collar.adapter.GroupMemberAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.UsersResultBean;
import com.codez.collar.databinding.ActivityBaseListBinding;
import com.codez.collar.event.NightModeChangedEvent;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GroupsMemberActivity extends BaseActivity<ActivityBaseListBinding>{
    private static final String TAG = "GroupsMemberActivity";
    public static final String INTENT_GROUP_ID = "group_id";
    private GroupMemberAdapter mAdapter;
    private String groupId;

    @Override
    public int setContent() {
        return R.layout.activity_base_list;
    }

    @Override
    public void initView() {
        EventBusUtils.register(this);
        setToolbarTitle(mBinding.toolbar, "分组好友");

        groupId = getIntent().getStringExtra(INTENT_GROUP_ID);
        mAdapter = new GroupMemberAdapter(this, groupId);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.swipeRefreshLayout.setEnabled(false);//取消下拉刷新
        mBinding.swipeRefreshLayout.setRefreshing(true);//设置初始时加载提示
        loadData();
    }

    private void loadData() {
        HttpUtils.getInstance().getFriendshipService()
                .getGroupsMember(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UsersResultBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                        mBinding.swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError"+e.toString());
                        mBinding.swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(UsersResultBean bean) {
                        mAdapter.setList(bean.getUsers());
                        mAdapter.notifyDataSetChanged();
                    }
                });
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
}
