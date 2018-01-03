package com.codez.collar.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.codez.collar.R;
import com.codez.collar.activity.PostActivity;
import com.codez.collar.activity.StatusDetailActivity;
import com.codez.collar.activity.UserActivity;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.bean.FavoriteBean;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.databinding.ItemStatusBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.ui.emojitextview.StatusContentTextUtil;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.L;
import com.codez.collar.utils.ScreenUtil;
import com.codez.collar.utils.T;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.BindingViewHolder>{

    private Context mContext;
    private OnChangeAlphaListener onChangeAlphaListener;
    private List<StatusBean> list;
    private int mType;

    public static final int TYPE_DEFAULT=0;
    public static final int TYPE_OWN =1;
    public static final int TYPE_FAVORITE = 2;


    public StatusAdapter(Context mContext, OnChangeAlphaListener onChangeAlphaListener) {
        this.mContext = mContext;
        this.onChangeAlphaListener = onChangeAlphaListener;
        this.list = new ArrayList<>();
        this.mType = TYPE_DEFAULT;
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

        public BindingViewHolder(ItemStatusBinding binding) {
            super(binding.llRoot);
            this.mBinding = binding;
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
                if (bean.getRetweeted_status().getUser() != null) {
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
                            mBundle.putSerializable(StatusBean.INTENT_SERIALIZABLE, bean.getRetweeted_status());
                            mContext.startActivity(new Intent(mContext, StatusDetailActivity.class)
                                    .putExtras(mBundle));
                        }
                    });
                }else{//微博已被删除情况，无user
                    mBinding.retweetedContent.setText(
                            StatusContentTextUtil.getWeiBoContent(bean.getRetweeted_status().getText(),
                                    mContext, mBinding.retweetedContent));
                }

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



            //status更多操作按钮 点击事件
            mBinding.ivMoreOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //背景虚化
                    onChangeAlphaListener.setAlpha(0.5f);
                    final Dialog dialog_more = new Dialog(mContext,R.style.DialogStatement);
                    dialog_more.setContentView(LayoutInflater.from(mContext)
                            .inflate(R.layout.dialog_status_more, null));
                    dialog_more.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //背景虚化恢复
                            onChangeAlphaListener.setAlpha(1.0f);
                        }
                    });
                    WindowManager.LayoutParams param = dialog_more.getWindow().getAttributes(); // 获取对话框当前的参数值
                    param.width = (int) (ScreenUtil.getScreenWidth(mContext) * 0.8); // 宽度设置为屏幕的
                    dialog_more.getWindow().setAttributes(param);
                    dialog_more.getWindow().setWindowAnimations(R.style.SelectPicStyle);//设置进出动画
                    final TextView tv_favorite = (TextView) dialog_more.findViewById(R.id.tv_favorite);
                    tv_favorite.setText((bean.isFavorited() ? "取消" : "")+ "收藏");
                    dialog_more.findViewById(R.id.tv_favorite).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (bean.isFavorited()) {
                                HttpUtils.getInstance().getFavoriteService(mContext)
                                        .destroyFavorite(AccessTokenKeeper.getAccessToken(mContext), bean.getId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<FavoriteBean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                T.s(mContext,"操作失败");
                                                L.e("onError:"+e.toString());
                                            }

                                            @Override
                                            public void onNext(FavoriteBean resultBean) {
                                                T.s(mContext, "已取消收藏");
                                                list.get(list.indexOf(bean)).setFavorited(false);
                                                tv_favorite.setText("收藏");
                                                tv_favorite.setSelected(false);
                                                //如果当前页面是"我的收藏"页面，则取消收藏后，需删除当前微博item
                                                if (mType == TYPE_FAVORITE) {
                                                    list.remove(list.indexOf(bean));
                                                    notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }else{
                                HttpUtils.getInstance().getFavoriteService(mContext)
                                        .createFavorite(AccessTokenKeeper.getAccessToken(mContext), bean.getId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<FavoriteBean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                T.s(mContext,"操作失败");
                                                L.e("onError:"+e.toString());
                                            }

                                            @Override
                                            public void onNext(FavoriteBean resultBean) {
                                                T.s(mContext, "已收藏");
                                                tv_favorite.setText("取消收藏");
                                                tv_favorite.setSelected(true);
                                                list.get(list.indexOf(bean)).setFavorited(true);
                                            }
                                        });
                            }

                            dialog_more.dismiss();
                        }
                    });


                    if (mType == TYPE_OWN) {
                        dialog_more.findViewById(R.id.tv_follow).setVisibility(View.GONE);
                        dialog_more.findViewById(R.id.tv_destroy).setVisibility(View.VISIBLE);
                        dialog_more.findViewById(R.id.tv_destroy).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HttpUtils.getInstance().getWeiboService(mContext)
                                        .destroyStatus(AccessTokenKeeper.getAccessToken(mContext), bean.getId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<StatusBean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                T.s(mContext, "操作失败");
                                            }

                                            @Override
                                            public void onNext(StatusBean bean) {
                                                T.s(mContext, "删除微博成功");
                                                notifyDataSetChanged();
                                            }
                                        });
                                dialog_more.dismiss();
                            }
                        });

                    }else{
                        dialog_more.findViewById(R.id.tv_follow).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                T.s(mContext,"TODO 关注");
                                dialog_more.dismiss();
                            }
                        });
                    }

                    dialog_more.show();
                }
            });
            mBinding.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, UserActivity.class)
                    .putExtra(UserActivity.INTENT_KEY_UID, bean.getUser().getId()), ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,mBinding.ivHead,"shareAvatar").toBundle());
                }
            });
            mBinding.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, UserActivity.class)
                            .putExtra(UserActivity.INTENT_KEY_UID, bean.getUser().getId()));
                }
            });
            mBinding.blockComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(StatusBean.INTENT_SERIALIZABLE, bean);
                    mContext.startActivity(new Intent(mContext, StatusDetailActivity.class)
                            .putExtras(mBundle)
                            .putExtra(StatusDetailActivity.INTENT_FROM_COMMENT, true));
                }
            });
            mBinding.blockRepost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(StatusBean.INTENT_SERIALIZABLE, bean);
                    mContext.startActivity(new Intent(mContext, PostActivity.class)
                            .putExtras(mBundle)
                            .putExtra(PostActivity.INTENT_REPOST, true));
                }
            });
            mBinding.executePendingBindings();
        }

        private void setStatusImage(RecyclerView recyclerView, ArrayList<StatusBean.PicUrlsBean> pic_urls) {
            if (pic_urls.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                return;
            } else if (pic_urls.size() == 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            }
            recyclerView.setNestedScrollingEnabled(false);
            UserAlbumAdapter mAdapter = new UserAlbumAdapter(mContext);
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

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public void setList(List<StatusBean> list) {
        this.list.clear();
        this.list = list;
    }
    public void addAll(List<StatusBean> list){
        this.list.addAll(list);
    }

    public void clearList() {
        this.list = new ArrayList<>();
    }

    public interface OnChangeAlphaListener{
        void setAlpha(float alpha);
    }
}
