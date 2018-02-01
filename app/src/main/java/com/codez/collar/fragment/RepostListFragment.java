package com.codez.collar.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.RepostAdapter;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.RepostResultBean;
import com.codez.collar.databinding.FragmentCommentListBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RepostListFragment extends BaseFragment<FragmentCommentListBinding> implements View.OnClickListener {

    private static final String KEY_ID = "id";


    private String mId;
    private String mScreenName;
    private int mSource;
    private int curPage;
    private RepostAdapter mAdapter;
    @Override
    public int setContent() {
        return R.layout.fragment_comment_list;
    }

    public static RepostListFragment newInstance(String id){
        RepostListFragment fragment = new RepostListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initView(View root) {
        if (getArguments() != null) {
            mId = getArguments().getString(KEY_ID);
        }
        curPage = 1;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.setHasFixedSize(true);
        mAdapter = new RepostAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
        loadData();

    }

    private void loadData() {
        HttpUtils.getInstance().getWeiboService()
                .getRepostStatus(mId, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RepostResultBean>() {
                    @Override
                    public void onCompleted() {
                        L.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError:"+e.toString());
                    }

                    @Override
                    public void onNext(RepostResultBean repostResultBean) {
                        mAdapter.addAll(repostResultBean.getReposts());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onClick(View v) {

    }
}
