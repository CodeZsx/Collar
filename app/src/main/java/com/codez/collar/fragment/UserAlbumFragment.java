package com.codez.collar.fragment;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.UserAlbumAdapter;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.bean.StatusResultBean;
import com.codez.collar.databinding.FragmentUserAlbumBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.L;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UserAlbumFragment extends BaseFragment<FragmentUserAlbumBinding> implements View.OnClickListener {

    private static final String KEY_UID = "uid";
    private static final String KEY_SCREEN_NAME = "screen_name";
    private String mUid;
    private String mScreenName;
    private int curPage;
    private UserAlbumAdapter mAdapter;
    @Override
    public int setContent() {
        return R.layout.fragment_user_album;
    }

    public static UserAlbumFragment newInstance(String uid, String screenName){
        UserAlbumFragment fragment = new UserAlbumFragment();
        Bundle args = new Bundle();
        args.putString(KEY_UID, uid);
        args.putString(KEY_SCREEN_NAME, screenName);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initView(View root) {
        if (getArguments() != null) {
            mUid = getArguments().getString(KEY_UID);
            mScreenName = getArguments().getString(KEY_SCREEN_NAME);
        }
        curPage = 1;

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new UserAlbumAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int itemPadding = DensityUtil.dp2px(getContext(), 4);
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
        loadData();

    }

    private void loadData() {
        HttpUtils.getInstance().getWeiboService()
                .getUserStatus(mUid, mScreenName, curPage++)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusResultBean>() {
                    @Override
                    public void onCompleted() {
                        L.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError:"+e.toString());
                    }

                    @Override
                    public void onNext(StatusResultBean statusResultBean) {
                        for (StatusBean status : statusResultBean.getStatuses()) {
                            if (status.getPic_urls().size() > 0) {
                                mAdapter.addAll(status.getPic_urls());
                            }else{
                                if (status.getRetweeted_status() != null) {
                                    mAdapter.addAll(status.getRetweeted_status().getPic_urls());
                                }
                            }
                        }
                        if (mAdapter.getList().size() < 16) {
                            loadData();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onClick(View v) {

    }
}
