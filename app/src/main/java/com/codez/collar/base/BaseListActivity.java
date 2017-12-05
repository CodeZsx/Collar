package com.codez.collar.base;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.StatusAdapter;
import com.codez.collar.databinding.ActivityBaseListBinding;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.T;

public abstract class BaseListActivity extends BaseActivity<ActivityBaseListBinding>{

    private StatusAdapter mAdapter;
    private int curPage = 1;

    @Override
    public int setContent() {
        return R.layout.activity_base_list;
    }

    @Override
    public void initView() {
        setToolbarTitle(mBinding.toolbar, "账号管理");

        mAdapter = new StatusAdapter(this, new StatusAdapter.OnChangeAlphaListener() {
            @Override
            public void setAlpha(float alpha) {
                setBgAlpha(alpha);
            }
        });
        mBinding.recyclerView.setAdapter(mAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int itemPadding = DensityUtil.dp2px(BaseListActivity.this, 8);
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
                    T.s(BaseListActivity.this, "加载更多");
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

    protected abstract void loadData();

}
