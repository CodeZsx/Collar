package com.codez.collar.activity;

import android.support.v7.widget.LinearLayoutManager;

import com.codez.collar.R;
import com.codez.collar.adapter.GroupRowAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.Group;
import com.codez.collar.databinding.ActivityBaseListBinding;
import com.codez.collar.event.GroupsUpdateEvent;
import com.codez.collar.manager.GroupManager;
import com.codez.collar.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class GroupsActivity extends BaseActivity<ActivityBaseListBinding>{
    private static final String TAG = "GroupsActivity";
    private GroupRowAdapter mAdapter;

    @Override
    public int setContent() {
        return R.layout.activity_base_list;
    }

    @Override
    public void initView() {
        EventBusUtils.register(this);
        setToolbarTitle(mBinding.toolbar, "我的分组");

        mAdapter = new GroupRowAdapter(this);
        mBinding.recyclerView.setAdapter(mAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.swipeRefreshLayout.setEnabled(false);//取消下拉刷新
        mBinding.swipeRefreshLayout.setRefreshing(true);
        loadData();
    }
    private void loadData() {
        List<Group> list = GroupManager.getInstance().getGroups();
        if (list != null) {
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
            mBinding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupsUpdateEvent(GroupsUpdateEvent event) {
        loadData();
    }

}
