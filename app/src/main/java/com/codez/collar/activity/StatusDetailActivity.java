package com.codez.collar.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.UserAlbumAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.databinding.ActivityStatusDetailBinding;
import com.codez.collar.fragment.CommentListFragment;
import com.codez.collar.fragment.RepostListFragment;
import com.codez.collar.ui.emojitextview.StatusContentTextUtil;
import com.codez.collar.utils.DensityUtil;

import java.util.ArrayList;

public class StatusDetailActivity extends BaseActivity<ActivityStatusDetailBinding> {

    public static String INTENT_FROM_COMMENT = "from_comment";
    private boolean isFromComment;


    @Override
    public int setContent() {
        return R.layout.activity_status_detail;
    }

    @Override
    public void initView() {
        setToolbarTitle(mBinding.toolbar, "微博正文");

        //获取intent传递过来的bean
        final StatusBean bean = (StatusBean) getIntent().getSerializableExtra(StatusBean.INTENT_SERIALIZABLE);
        isFromComment = getIntent().getBooleanExtra(INTENT_FROM_COMMENT, false);

        if (isFromComment) {
            mBinding.appbar.setExpanded(false);
        }

        mBinding.setStatus(bean);
        //微博正文
        mBinding.tvContent.setText(StatusContentTextUtil.getWeiBoContent(bean.getText(),
                this, mBinding.tvContent));
        //微博图片，根据无图片、多张图片进行不同的显示方式
        setStatusImage(mBinding.recyclerView, bean.getPic_urls());
        //转发微博体
        if (bean.getRetweeted_status()==null){
            mBinding.llRetweeted.setVisibility(View.GONE);
        }else{
            //转发微博体的正文
            mBinding.retweetedContent.setText(
                    StatusContentTextUtil.getWeiBoContent(
                            "@" + bean.getRetweeted_status().getUser().getScreen_name() +
                                    ":" + bean.getRetweeted_status().getText(),
                            this, mBinding.retweetedContent));
            //转发微博体的图片
            setStatusImage(mBinding.retweetedRecyclerView, bean.getRetweeted_status().getPic_urls());

            mBinding.llRetweeted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(StatusBean.INTENT_SERIALIZABLE, bean);
                    StatusDetailActivity.this.startActivity(
                            new Intent(StatusDetailActivity.this, StatusDetailActivity.class)
                            .putExtras(mBundle));
                }
            });
        }


        mBinding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            String[] titles = {"评论 "+bean.getComments_count(), "转发 "+bean.getReposts_count()};
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {

                    return new CommentListFragment().newInstance(bean.getId());
                }else{
                    return new RepostListFragment().newInstance(bean.getId());
                }
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    private void setStatusImage(RecyclerView recyclerView, ArrayList<StatusBean.PicUrlsBean> pic_urls) {
        if (pic_urls.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            return;
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        recyclerView.setNestedScrollingEnabled(false);
        UserAlbumAdapter mAdapter = new UserAlbumAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int itemPadding = DensityUtil.dp2px(StatusDetailActivity.this, 4);

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
        mAdapter.addAll(pic_urls);
        mAdapter.notifyDataSetChanged();
    }
}
