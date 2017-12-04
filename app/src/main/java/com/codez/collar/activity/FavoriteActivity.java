package com.codez.collar.activity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.StatusAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.FavoriteBean;
import com.codez.collar.bean.FavoriteResultBean;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.databinding.ActivityTopicBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.T;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FavoriteActivity extends BaseActivity<ActivityTopicBinding> implements View.OnClickListener{

    private int curPage = 1;
    private StatusAdapter mAdapter;

    @Override
    public int setContent() {
        return R.layout.activity_topic;
    }

    @Override
    public void initView() {
        setToolbarTitle(mBinding.toolbar, "我的收藏");

        mAdapter = new StatusAdapter(this, new StatusAdapter.OnChangeAlphaListener() {
            @Override
            public void setAlpha(float alpha) {
                setBgAlpha(alpha);
            }
        });
        mAdapter.setType(StatusAdapter.TYPE_FAVORITE);
        mBinding.recyclerView.setAdapter(mAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int itemPadding = DensityUtil.dp2px(FavoriteActivity.this, 8);
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
        mBinding.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    T.s(FavoriteActivity.this, "加载更多");
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        mBinding.swipeRefreshLayout.setRefreshing(true);
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
        HttpUtils.getInstance().getFavoriteService(this)
                .getFavorite(curPage++)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FavoriteResultBean>() {
                    @Override
                    public void onCompleted() {
                        mBinding.swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FavoriteResultBean resultBean) {
                        List<StatusBean> list = new ArrayList<>();
                        for (FavoriteBean favorite : resultBean.getFavorites()) {
                            list.add(favorite.getStatus());
                        }
                        mAdapter.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
