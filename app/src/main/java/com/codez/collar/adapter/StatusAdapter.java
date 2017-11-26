package com.codez.collar.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;
import com.codez.collar.activity.StatusDetailActivity;
import com.codez.collar.activity.UserActivity;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.databinding.ItemStatusBinding;
import com.codez.collar.ui.emojitextview.StatusContentTextUtil;
import com.codez.collar.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.BindingViewHolder> {

    private Context mContext;
    private List<StatusBean> list;

    public StatusAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        ItemStatusBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_status, parent, false);
        return new BindingViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        holder.bindItem(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BindingViewHolder extends RecyclerView.ViewHolder {

        private ItemStatusBinding mBinding;

        public BindingViewHolder(ItemStatusBinding itemStatusBinding) {
            super(itemStatusBinding.llRoot);
            this.mBinding = itemStatusBinding;
        }
        private void bindItem(final StatusBean bean){
            mBinding.setStatus(bean);

            //微博正文
            mBinding.tvContent.setText(StatusContentTextUtil.getWeiBoContent(bean.getText(),
                    mContext, mBinding.tvContent));

            if (bean.getUser().isVerified()) {
                mBinding.ivVip.setVisibility(View.VISIBLE);
            }
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
                                mContext, mBinding.retweetedContent));
                //转发微博体的图片
                setStatusImage(mBinding.retweetedRecyclerView, bean.getRetweeted_status().getPic_urls());

                mBinding.llRetweeted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable(StatusBean.INTENT_SERIALIZABLE, bean);
                        mContext.startActivity(new Intent(mContext, StatusDetailActivity.class)
                                .putExtras(mBundle));
                    }
                });
            }


            //点击事件
            mBinding.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(StatusBean.INTENT_SERIALIZABLE, bean);
                    mContext.startActivity(new Intent(mContext, StatusDetailActivity.class)
                            .putExtras(mBundle));
                }
            });
            mBinding.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, UserActivity.class)
                    .putExtra(UserActivity.INTENT_KEY_UID, bean.getUser().getId()));
                }
            });
            mBinding.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, UserActivity.class)
                            .putExtra(UserActivity.INTENT_KEY_UID, bean.getUser().getId()));
                }
            });
            mBinding.executePendingBindings();
        }

        private void setStatusImage(RecyclerView recyclerView, ArrayList<StatusBean.PicUrlsBean> pic_urls) {
            if (pic_urls.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                return;
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            }
            recyclerView.setNestedScrollingEnabled(false);
            AlbumAdapter mAdapter = new AlbumAdapter(mContext);
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                int itemPadding = DensityUtil.dp2px(mContext, 4);

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


    public void setList(List<StatusBean> list) {
        this.list.clear();
        this.list = list;
    }
    public void addAll(List<StatusBean> list){
        this.list.addAll(list);
    }
}
