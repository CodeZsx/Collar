package com.codez.collar.fragment;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.StatusAdapter;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.StatusResultBean;
import com.codez.collar.databinding.FragmentStatusListBinding;
import com.codez.collar.databinding.ItemRvFooterBinding;
import com.codez.collar.listener.EndlessRecyclerViewOnScrollListener;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.ui.recyclerview.HeaderAndFooterWrapper;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.L;
import com.codez.collar.utils.T;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StatusListFragment extends BaseFragment<FragmentStatusListBinding> implements View.OnClickListener {

    private static final String KEY_UID = "uid";
    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_SOURCE = "source";
    public static final int VALUE_USER = 1;
    public static final int VALUE_HOME = 2;
    public static final int VALUE_PUBLIC = 3;
    public static final int VALUE_MENTION = 4;


    private String mUid;
    private String mScreenName;
    private int mSource;
    private int curPage;
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
        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                mStatusAdapter.clearList();
                loadData();
            }
        });
        mBinding.swipeRefreshLayout.setRefreshing(true);

        loadData();

    }


    private void loadData() {

        switch (mSource) {
            case VALUE_HOME:
                HttpUtils.getInstance().getWeiboService(getContext())
                        .getHomeStatus(mUid, curPage++)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<StatusResultBean>() {
                            @Override
                            public void onCompleted() {
                                L.e("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                handleError(e);
                            }

                            @Override
                            public void onNext(StatusResultBean statusResultBean) {
                                handleData(statusResultBean);
                            }
                        });
                break;
            case VALUE_PUBLIC:
                HttpUtils.getInstance().getWeiboService(getContext())
                        .getPublicStatus(curPage++)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<StatusResultBean>() {
                            @Override
                            public void onCompleted() {
                                L.e("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                handleError(e);
                            }

                            @Override
                            public void onNext(StatusResultBean statusResultBean) {
                                handleData(statusResultBean);
                            }
                        });
                break;
            case VALUE_MENTION:
                HttpUtils.getInstance().getWeiboService(getContext())
                        .getStatusMention(curPage++)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<StatusResultBean>() {
                            @Override
                            public void onCompleted() {
                                L.e("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                handleError(e);
                            }

                            @Override
                            public void onNext(StatusResultBean statusResultBean) {
                                handleData(statusResultBean);
                            }
                        });

            case VALUE_USER:
            default:
                mStatusAdapter.setType(StatusAdapter.TYPE_OWN);
                HttpUtils.getInstance().getWeiboService(getContext())
                        .getUserStatus(mUid, mScreenName,curPage++)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<StatusResultBean>() {
                            @Override
                            public void onCompleted() {
                                L.e("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                handleError(e);
                            }

                            @Override
                            public void onNext(StatusResultBean statusResultBean) {
                                handleData(statusResultBean);
                            }
                        });
                break;
        }

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

    private void handleData(StatusResultBean statusResultBean) {
//        mStatusAdapter.addAll(statusResultBean.getStatuses());
//        mWrapper.notifyDataSetChanged();
        mStatusAdapter.addAll(statusResultBean.getStatuses());
        mStatusAdapter.notifyDataSetChanged();
        mBinding.swipeRefreshLayout.setRefreshing(false);
    }

    private void handleError(Throwable e) {
        L.e(e.toString());
        T.s(getContext(), "请求数据失败");
    }


    @Override
    public void onClick(View v) {

    }
}
