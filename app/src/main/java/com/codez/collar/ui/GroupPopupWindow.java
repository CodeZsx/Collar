package com.codez.collar.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

import com.codez.collar.R;
import com.codez.collar.adapter.GroupAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.Group;
import com.codez.collar.event.GroupsUpdateEvent;
import com.codez.collar.manager.GroupManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by codez on 2018/2/8.
 * Description:
 */

public class GroupPopupWindow extends PopupWindow{

    private static final String TAG = "GroupPopupWindow";
    private Context mContext;
    private GroupPopupWindow mWindow;
    private View mView;
    private GroupAdapter mAdapter;

    public GroupPopupWindow(Context context, int width, int height) {
        super(context);
        this.mContext = context;
        mView = ((BaseActivity)context).getLayoutInflater().inflate(R.layout.popup_groups, null);
        setContentView(mView);
        setWidth(width);
        setHeight(height);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.GroupChooseWindowStyle);
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(manager);
        mAdapter = new GroupAdapter(context);
        recyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        mAdapter.clear();
        Log.i("GroupManager", "initData");
        List<Group> list = GroupManager.getInstance().getGroups();
        if (list != null) {
            Log.i("GroupPopupWindow", "list:" + list.size());
            mAdapter.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupsUpdateEvent(GroupsUpdateEvent event) {
        Log.i(TAG, "onGroupsUpdateEvent:");
        initData();
    }
}
