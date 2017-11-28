package com.codez.collar.activity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.StatusAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.TopicResultBean;
import com.codez.collar.databinding.ActivityTopicBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.L;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TopicActivity extends BaseActivity<ActivityTopicBinding> implements View.OnClickListener{

    public static final String INTENT_TOPIC = "topic";
    private String mTopic;
    private int curPage;
    private StatusAdapter mAdapter;

    @Override
    public int setContent() {
        return R.layout.activity_topic;
    }

    @Override
    public void initView() {
        mTopic = getIntent().getStringExtra(INTENT_TOPIC);
        setToolbarTitle(mBinding.toolbar, mTopic);
        mTopic = mTopic.substring(1, mTopic.length()-1);
        L.e(mTopic);

        mAdapter = new StatusAdapter(this);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int itemPadding = DensityUtil.dp2px(TopicActivity.this, 8);
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
        curPage = 1;
        loadData();
    }

    private void loadData() {
        HttpUtils.getInstance().getSearchService(this)
                .getSearchTopics(mTopic, curPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TopicResultBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TopicResultBean topicResultBean) {
                        mAdapter.addAll(topicResultBean.getStatuses());
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
