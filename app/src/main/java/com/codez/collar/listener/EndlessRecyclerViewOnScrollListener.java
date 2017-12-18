package com.codez.collar.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by codez on 2017/12/17.
 * Description:
 */

public class EndlessRecyclerViewOnScrollListener extends RecyclerView.OnScrollListener{

    private static final int LINEARLAYOUT = 1;
    private static final int GRIDLAYOUT = 2;
    private static final int STAGGEREDGRIDLAYOUT = 3;

    /**
     * 当前RecyclerView类型
     */
    private int layoutManagerType;
    /**
     * 最后一个的位置
     */
    private int[] lastPositions;
    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;
    /**
     * 当前滑动的状态
     */
    private int currentScrollState = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LINEARLAYOUT;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = GRIDLAYOUT;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = STAGGEREDGRIDLAYOUT;
            }else{
                throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }
        switch (layoutManagerType){
            case LINEARLAYOUT:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GRIDLAYOUT:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case STAGGEREDGRIDLAYOUT:
                StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);

                lastVisibleItemPosition = lastPositions[0];
                for (int value : lastPositions) {
                    lastVisibleItemPosition = value > lastVisibleItemPosition ? value : lastVisibleItemPosition;
                }
                break;
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        currentScrollState = newState;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItemPosition) >= totalItemCount - 1)) {
            onLoadNextPage(recyclerView);
        }
    }

    public void onLoadNextPage(View view) {
    }
}
