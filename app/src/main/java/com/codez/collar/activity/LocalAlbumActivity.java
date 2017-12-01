package com.codez.collar.activity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.LocalAlbumAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.AlbumFolder;
import com.codez.collar.databinding.ActivityLocalAlbumBinding;
import com.codez.collar.tools.ImageScan;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.T;

import java.io.File;
import java.util.ArrayList;

public class LocalAlbumActivity extends BaseActivity<ActivityLocalAlbumBinding> implements View.OnClickListener{

    private ArrayList<AlbumFolder> mList = new ArrayList<>();
    private LocalAlbumAdapter mAdapter;
    @Override
    public int setContent() {
        return R.layout.activity_local_album;
    }

    @Override
    public void initView() {
        setToolbarTitle(mBinding.toolbar, "所有");

        mBinding.setCount(0);

        mAdapter = new LocalAlbumAdapter(this);
        mAdapter.addSelectedCountChangedListener(new LocalAlbumAdapter.OnSelectedCountChangedListener() {
            @Override
            public void onChanged(int count) {
                mBinding.tvCommit.setSelected(count>0);
                mBinding.setCount(count);
            }
        });
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mBinding.swipeRefreshLayout.setRefreshing(true);
        mBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int itemPadding = DensityUtil.dp2px(LocalAlbumActivity.this, 4);
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = itemPadding;
                outRect.left = itemPadding;
                outRect.right = itemPadding;
                outRect.top = itemPadding;
            }
        });

        new ImageScan(this, getLoaderManager()){

            @Override
            public void onScanFinish(ArrayList<AlbumFolder> folders) {
                mBinding.swipeRefreshLayout.setRefreshing(false);
                mList = folders;
                ArrayList<String> list = new ArrayList<>();
                for (File file : mList.get(0).getImageList()) {
                    list.add(file.getAbsolutePath());
                }
                mAdapter.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
        };

        mBinding.tvCommit.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                //TODO
                T.s(this, "完成");
                break;
        }
    }
}
