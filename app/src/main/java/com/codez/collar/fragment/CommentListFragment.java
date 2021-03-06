package com.codez.collar.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.CommentAdapter;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.CommentResultBean;
import com.codez.collar.databinding.FragmentCommentListBinding;
import com.codez.collar.net.HttpUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CommentListFragment extends BaseFragment<FragmentCommentListBinding> implements View.OnClickListener {

    private static final String TAG = "CommentListFragment";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";

    public static final int TYPE_COMMENT_STATUS_DETAIL = 0;
    public static final int TYPE_COMMENT_TO_ME = 1;
    public static final int TYPE_COMMENT_BY_ME = 2;
    public static final int TYPE_COMMENT_MENTION = 3;


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
            case TYPE_COMMENT_STATUS_DETAIL:
                mAdapter.setType(CommentAdapter.TYPE_COMMENT_NO_STATUS);
                HttpUtils.getInstance().getCommentService()
                        .getStatusComment(mId, 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CommentResultBean>() {
                            @Override
                            public void onCompleted() {}

                            @Override
                            public void onError(Throwable e) {
                                Log.w(TAG, "onError:"+e.toString());
                            }

                            @Override
                            public void onNext(CommentResultBean commentResultBean) {
                                mAdapter.addAll(commentResultBean.getComments());
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                break;
            case TYPE_COMMENT_TO_ME:
                mAdapter.setType(CommentAdapter.TYPE_COMMENT_STATUS);
                HttpUtils.getInstance().getCommentService()
                        .getCommentToMe(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CommentResultBean>() {
                            @Override
                            public void onCompleted() {}

                            @Override
                            public void onError(Throwable e) {
                                Log.w(TAG, "onError:"+e.toString());
                            }

                            @Override
                            public void onNext(CommentResultBean commentResultBean) {
                                mAdapter.addAll(commentResultBean.getComments());
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                break;
            case TYPE_COMMENT_BY_ME:
                mAdapter.setType(CommentAdapter.TYPE_COMMENT_STATUS);
                HttpUtils.getInstance().getCommentService()
                        .getCommentByMe(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CommentResultBean>() {
                            @Override
                            public void onCompleted() {}

                            @Override
                            public void onError(Throwable e) {
                                Log.w(TAG, "onError:"+e.toString());
                            }

                            @Override
                            public void onNext(CommentResultBean commentResultBean) {
                                mAdapter.addAll(commentResultBean.getComments());
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                break;

            case TYPE_COMMENT_MENTION:
                mAdapter.setType(CommentAdapter.TYPE_COMMENT_STATUS);
                HttpUtils.getInstance().getCommentService()
                        .getCommentMentions(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CommentResultBean>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError:"+e.toString());
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
