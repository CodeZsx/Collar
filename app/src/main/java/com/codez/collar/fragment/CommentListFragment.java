package com.codez.collar.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.CommentAdapter;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.CommentResultBean;
import com.codez.collar.databinding.FragmentCommentListBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CommentListFragment extends BaseFragment<FragmentCommentListBinding> implements View.OnClickListener {

    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";


    private String mId;
    private int mType;
    private String mScreenName;
    private int mSource;
    private int curPage;
    private CommentAdapter mAdapter;
    @Override
    public int setContent() {
        return R.layout.fragment_comment_list;
    }

    public static CommentListFragment newInstance(String id,int type){
        CommentListFragment fragment = new CommentListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        args.putInt(KEY_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initView(View root) {
        if (getArguments() != null) {
            mId = getArguments().getString(KEY_ID);
            mType = getArguments().getInt(KEY_TYPE);
        }
        curPage = 1;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.setHasFixedSize(true);
        mAdapter = new CommentAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
        loadData();

    }

    private void loadData() {
        switch (mType) {
            case CommentAdapter.TYPE_COMMENT_STATUS:
                HttpUtils.getInstance().getCommentService(getContext())
                        .getStatusComment(mId, 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CommentResultBean>() {
                            @Override
                            public void onCompleted() {
                                L.e("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                L.e("onError:"+e.toString());
                            }

                            @Override
                            public void onNext(CommentResultBean commentResultBean) {
                                mAdapter.addAll(commentResultBean.getComments());
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                break;
            case CommentAdapter.TYPE_COMMENT_TO_ME:
                mAdapter.setType(CommentAdapter.TYPE_COMMENT_TO_ME);
                HttpUtils.getInstance().getCommentService(getContext())
                        .getCommentToMe(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CommentResultBean>() {
                            @Override
                            public void onCompleted() {
                                L.e("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                L.e("onError:"+e.toString());
                            }

                            @Override
                            public void onNext(CommentResultBean commentResultBean) {
                                mAdapter.addAll(commentResultBean.getComments());
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                break;
            case CommentAdapter.TYPE_COMMENT_BY_ME:
                mAdapter.setType(CommentAdapter.TYPE_COMMENT_BY_ME);
                HttpUtils.getInstance().getCommentService(getContext())
                        .getCommentByMe(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CommentResultBean>() {
                            @Override
                            public void onCompleted() {
                                L.e("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                L.e("onError:"+e.toString());
                            }

                            @Override
                            public void onNext(CommentResultBean commentResultBean) {
                                mAdapter.addAll(commentResultBean.getComments());
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                break;
        }

    }


    @Override
    public void onClick(View v) {

    }
}
