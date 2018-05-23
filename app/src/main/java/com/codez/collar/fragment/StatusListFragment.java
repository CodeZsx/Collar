package com.codez.collar.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.codez.collar.Config;
import com.codez.collar.R;
import com.codez.collar.adapter.StatusAdapter;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.Group;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.bean.StatusResultBean;
import com.codez.collar.databinding.FragmentStatusListBinding;
import com.codez.collar.databinding.ItemRvFooterBinding;
import com.codez.collar.db.DBConstants;
import com.codez.collar.db.DataBaseHelper;
import com.codez.collar.event.GroupChangedEvent;
import com.codez.collar.event.NightModeChangedEvent;
import com.codez.collar.event.ToastEvent;
import com.codez.collar.listener.EndlessRecyclerViewOnScrollListener;
import com.codez.collar.manager.GroupManager;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.ui.recyclerview.HeaderAndFooterWrapper;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.EventBusUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StatusListFragment extends BaseFragment<FragmentStatusListBinding> implements View.OnClickListener {

    private static final String TAG = "StatusListFragment";
    private static final String KEY_UID = "uid";
    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_SOURCE = "source";
    public static final int VALUE_USER = 1;
    public static final int VALUE_HOME = 2;
    public static final int VALUE_PUBLIC = 3;
    public static final int VALUE_MENTION = 4;


    private String mUid;
    //既代表User的screenName,又代表首页的分组名（全部、特别关注等）
    private String mScreenName;
    private int mSource;
    private int curPage;
    private Group mGroup;
    private StatusAdapter mStatusAdapter;
    private HeaderAndFooterWrapper mWrapper;
    private ItemRvFooterBinding mFooterBinding;
    @Override
    public int setContent() {
        return R.layout.fragment_status_list;
    }

    public static StatusListFragment newInstance(String uid, String screen_name, int source){
        StatusListFragment fragment = new StatusListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_UID, uid);
        args.putString(KEY_SCREEN_NAME, screen_name);
        args.putInt(KEY_SOURCE, source);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initView(View root) {
        EventBusUtils.register(this);
        if (getArguments() != null) {
            mUid = getArguments().getString(KEY_UID);
            mScreenName = getArguments().getString(KEY_SCREEN_NAME);
            mSource = getArguments().getInt(KEY_SOURCE);
        }
        curPage = 1;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.setHasFixedSize(true);
        mStatusAdapter = new StatusAdapter(getContext(), new StatusAdapter.OnChangeAlphaListener() {
            @Override
            public void setAlpha(float alpha) {
                setBgAlpha(alpha);
            }
        });

//        mWrapper = new HeaderAndFooterWrapper(mStatusAdapter);
//        mFooterBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.item_rv_footer, null, false);
//        mWrapper.addFooterView(mFooterBinding.llRoot);
//        mBinding.recyclerView.setAdapter(mWrapper);
        mBinding.recyclerView.setAdapter(mStatusAdapter);

        mBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int itemPadding = DensityUtil.dp2px(getContext(), 8);
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = itemPadding;
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top=itemPadding;
                }
            }
        });
        mBinding.recyclerView.addOnScrollListener(new EndlessRecyclerViewOnScrollListener(){
            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                changeFooterState(STATE_LOADING);
                loadData();
            }
        });
        EventBusUtils.sendEvent(new NightModeChangedEvent(Config.getCachedNight(getContext())));
        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                loadData();
            }
        });

        loadData();

    }


    private void loadData() {
        if (curPage == 1) {
            mStatusAdapter.clearList();
            mBinding.swipeRefreshLayout.setRefreshing(true);
        }

        switch (mSource) {
            case VALUE_HOME:
                if (HomeFragment.STATUS_GROUP_ALL.equals(mScreenName)) {

                    List<StatusBean> statuses = getStatusesFromDB(DBConstants.STATUS_TYPE_HOME);
                    if (statuses != null) {
                        handleData(statuses);
                    }
                    HttpUtils.getInstance().getWeiboService()
                            .getHomeStatus(mUid, curPage++)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<StatusResultBean>() {
                                @Override
                                public void onCompleted() {
                                    Log.i(TAG, "onCompleted");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    handleError(e);
                                }

                                @Override
                                public void onNext(StatusResultBean statusResultBean) {
                                    handleData(statusResultBean.getStatuses());
                                    saveStatusesToDB(statusResultBean.getStatuses(), DBConstants.STATUS_TYPE_HOME);
                                }
                            });
                }else{
                    if (mGroup == null) {
                        Log.i(TAG, "group first null");
                        mGroup = GroupManager.getInstance().getGroupsByName(mScreenName);
                    }
                    if (mGroup == null) {
                        Log.i(TAG, "group second null");
                        EventBusUtils.sendEvent(ToastEvent.newToastEvent("不存在此分组"));
                        return;
                    }
                    Log.i(TAG, "group id:" + mGroup.getIdstr());
                    HttpUtils.getInstance().getFriendshipService()
                            .getGroupsStatus(mGroup.getIdstr(), curPage++)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<StatusResultBean>() {
                                @Override
                                public void onCompleted() {
                                    Log.i(TAG, "onCompleted");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    handleError(e);
                                }

                                @Override
                                public void onNext(StatusResultBean statusResultBean) {
                                    handleData(statusResultBean.getStatuses());
                                }
                            });
                }

                break;
            case VALUE_PUBLIC:
                HttpUtils.getInstance().getWeiboService()
                        .getPublicStatus(curPage++)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<StatusResultBean>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                handleError(e);
                            }

                            @Override
                            public void onNext(StatusResultBean statusResultBean) {
                                handleData(statusResultBean.getStatuses());
                            }
                        });
                break;
            case VALUE_MENTION:
                HttpUtils.getInstance().getWeiboService()
                        .getStatusMention(curPage++)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<StatusResultBean>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                handleError(e);
                            }

                            @Override
                            public void onNext(StatusResultBean statusResultBean) {
                                handleData(statusResultBean.getStatuses());
                            }
                        });

            case VALUE_USER:
            default:
                if (mUid != null && mUid.equals(AccessTokenKeeper.getInstance().getUid())){
                    mStatusAdapter.setType(StatusAdapter.TYPE_OWN);
                }
                HttpUtils.getInstance().getWeiboService()
                        .getUserStatus(mUid, mScreenName,curPage++)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<StatusResultBean>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                handleError(e);
                            }

                            @Override
                            public void onNext(StatusResultBean statusResultBean) {
                                handleData(statusResultBean.getStatuses());
                            }
                        });
                break;
        }

    }

    private void saveStatusesToDB(List<StatusBean> statuses, String type) {
        for (StatusBean bean : statuses) {
            ContentValues values = new ContentValues();
            values.put(DBConstants.STATUS_COLUMN_ID, bean.getIdstr());
            values.put(DBConstants.STATUS_COLUMN_CONTENT, new Gson().toJson(bean));
            values.put(DBConstants.STATUS_COLUMN_TYPE, type);
            DataBaseHelper.getDataBaseHelper().insert(DBConstants.TABLE_STATUS, values);
        }
    }
    private List<StatusBean> getStatusesFromDB(String type){
        return DataBaseHelper.getDataBaseHelper().queryForObject(new DataBaseHelper.RowMapper<List<StatusBean>>() {
            @Override
            public List<StatusBean> mapRow(Cursor cursor, int index) {
                List<StatusBean> list = new ArrayList<>();
                if (cursor.moveToFirst()){
                    while (cursor.moveToNext()) {
                        String content = cursor.getString(cursor.getColumnIndex(DBConstants.STATUS_COLUMN_CONTENT));
                        if (content != null && TextUtils.isEmpty(content)) {
                            StatusBean bean = new Gson().fromJson(content, StatusBean.class);
                            list.add(bean);
                        }
                        if (list.size() > 10) {
                            break;
                        }
                    }
                }
                return list;
            }
        }, "SELECT * FROM " + DBConstants.TABLE_STATUS + " WHERE " + DBConstants.STATUS_COLUMN_TYPE + "=?",
                new String[]{type});
    }

    private static final int STATE_NORMAL = 0;
    private static final int STATE_LOADING = 1;
    private static final int STATE_END = 2;
    private static final int STATE_ERROR = 3;
    private void changeFooterState(int state) {
//        switch (state) {
//            case STATE_NORMAL:
//                mFooterBinding.tvInfo.setText("更多");
//                break;
//            case STATE_LOADING:
//                mFooterBinding.tvInfo.setText("正在加载中");
//                break;
//            case STATE_END:
//                mFooterBinding.tvInfo.setText("已经到底了");
//                break;
//            case STATE_ERROR:
//                mFooterBinding.tvInfo.setText("网络错误");
//                break;
//        }
    }

    private void handleData(List<StatusBean> statuses) {
        if (curPage == 2) {
            mStatusAdapter.clearList();
        }
        mStatusAdapter.addAll(statuses);
        mStatusAdapter.notifyDataSetChanged();
        mBinding.swipeRefreshLayout.setRefreshing(false);
    }

    private void handleError(Throwable e) {
        Log.e(TAG, "onError:" + e.toString());
        mBinding.swipeRefreshLayout.setRefreshing(false);
        EventBusUtils.sendEvent(ToastEvent.newToastEvent("请求数据失败"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupChangedEvent(GroupChangedEvent event) {
        Log.i(TAG, "onGroupChangedEvent");
        if (event == null) {
            return;
        }
        if (mSource == VALUE_HOME) {
            if (!mScreenName.equals(event.getName())) {
                Log.i(TAG, "name:" + event.getName());
                mScreenName = event.getName();
                mGroup = GroupManager.getInstance().getGroupsByName(mScreenName);
                curPage = 1;
            }
            loadData();
        }
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

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }
}
