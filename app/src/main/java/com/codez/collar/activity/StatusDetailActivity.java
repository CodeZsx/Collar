package com.codez.collar.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.codez.collar.R;
import com.codez.collar.adapter.UserAlbumAdapter;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.CommentBean;
import com.codez.collar.bean.FavoriteBean;
import com.codez.collar.bean.FriendshipsShowResultBean;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.ActivityStatusDetailBinding;
import com.codez.collar.event.ToastEvent;
import com.codez.collar.fragment.CommentListFragment;
import com.codez.collar.fragment.RepostListFragment;
import com.codez.collar.manager.AttitudesManager;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.ui.emojitextview.StatusContentTextUtil;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.ScreenUtil;
import com.codez.collar.utils.T;
import com.codez.collar.utils.Tools;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StatusDetailActivity extends BaseActivity<ActivityStatusDetailBinding> implements View.OnClickListener{

    private static final String TAG = "StatusDetailActivity";
    public static String INTENT_FROM_COMMENT = "from_comment";
    private static final int COMMENT_MAX_LENGTH = 140;
    private boolean isFromComment;

    private StatusBean mBean = null;
    public static final String BTN_FOLLOW = "关注";
    public static final String BTN_FOLLOWING = "已关注";


    @Override
    public int setContent() {
        return R.layout.activity_status_detail;
    }

    @Override
    public void initView() {
        setToolbarTitle(mBinding.toolbar, "微博正文");

        //获取intent传递过来的bean
        mBean = (StatusBean) getIntent().getSerializableExtra(StatusBean.INTENT_SERIALIZABLE);
        isFromComment = getIntent().getBooleanExtra(INTENT_FROM_COMMENT, false);

        if (isFromComment) {
            mBinding.appbar.setExpanded(false);
        }

        mBinding.setStatus(mBean);
        //微博正文
        mBinding.tvContent.setText(StatusContentTextUtil.getWeiBoContent(mBean.getText(),
                this, mBinding.tvContent));
        //微博图片，根据无图片、多张图片进行不同的显示方式
        setStatusImage(mBinding.recyclerView, mBean.getPic_urls());
        //转发微博体
        if (mBean.getRetweeted_status()==null){
            mBinding.llRetweeted.setVisibility(View.GONE);
        }else{
            //转发微博体的正文
            mBinding.retweetedContent.setText(
                    StatusContentTextUtil.getWeiBoContent(
                            "@" + mBean.getRetweeted_status().getUser().getScreen_name() +
                                    ":" + mBean.getRetweeted_status().getText(),
                            this, mBinding.retweetedContent));
            //转发微博体的图片
            setStatusImage(mBinding.retweetedRecyclerView, mBean.getRetweeted_status().getPic_urls());
            mBinding.llRetweeted.setOnClickListener(this);
        }

        //点赞按钮状态
        int likeState = AttitudesManager.getInstance().getAttitude(mBean.getIdstr());
        //welike没有点赞权限
        mBinding.ivLike.setSelected(likeState==AttitudesManager.STATE_LIKE);
//        if (likeState == -1) {
//            HttpUtils.getInstance().getAttitudesService()
//                    .existsLike(mBean.getIdstr())
//                    .enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            try {
//                                int state = JsonUtil.getValueByUncertainKey(response.body().bytes());
//                                AttitudesManager.getInstance().putAttitude(mBean.getIdstr(), state);
//                                mBinding.ivLike.setSelected(state == AttitudesManager.STATE_LIKE);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            Log.i(TAG, "onFailure:" + t.toString());
//                        }
//                    });
//        }else{
//            mBinding.ivLike.setSelected(likeState == AttitudesManager.STATE_LIKE);
//        }
        //设置评论和转发列表
        mBinding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            String[] titles = {"评论 "+ mBean.getComments_count(), "转发 "+ mBean.getReposts_count()};
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new CommentListFragment().newInstance(mBean.getId(), CommentListFragment.TYPE_COMMENT_STATUS_DETAIL);
                }else{
                    return new RepostListFragment().newInstance(mBean.getId());
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

        //设置edittext
        mBinding.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(s.toString())) {
                    mBinding.ivCommit.setSelected(false);
                }else{
                    mBinding.ivCommit.setSelected(true);
                }
                mBinding.tvStatusLength.setText((COMMENT_MAX_LENGTH - s.length())+"");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mBinding.etContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mBinding.rlAdditional.setVisibility(View.VISIBLE);
                }else{
                    mBinding.rlAdditional.setVisibility(View.GONE);
                }
            }
        });

        mBinding.blockLike.setOnClickListener(this);
        mBinding.blockComment.setOnClickListener(this);
        mBinding.blockRepost.setOnClickListener(this);
        mBinding.ivCommit.setOnClickListener(this);
        mBinding.ivHead.setOnClickListener(this);
        mBinding.ivMoreOption.setOnClickListener(this);

        reloadData();
    }

    private void reloadData() {
        HttpUtils.getInstance().getWeiboService()
                .getStatusInfo(mBean.getIdstr())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w(TAG, "onError:" + e.toString());
                    }

                    @Override
                    public void onNext(StatusBean statusBean) {
                        mBinding.tabLayout.getTabAt(0).setText("评论 " + statusBean.getComments_count());
                        mBinding.tabLayout.getTabAt(1).setText("转发 " + statusBean.getReposts_count());
                    }
                });
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
    private void createComment() {
        //对微博内容长度进行判断，超过STATUS_MAX_LENGTH（140）则不予发送
        if (mBinding.etContent.length() > COMMENT_MAX_LENGTH) {
            T.s(this,"评论无法发送，内容长度超过140个字符！");
            return;
        }
        //发送评论内容
        HttpUtils.getInstance().getCommentService()
                .createComment(AccessTokenKeeper.getInstance().getAccessToken(), mBinding.etContent.getText().toString(), mBean.getIdstr(),0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        T.s(StatusDetailActivity.this, "操作失败");
                    }

                    @Override
                    public void onNext(CommentBean commentBean) {
                        T.s(StatusDetailActivity.this, "评论成功");
                        mBinding.etContent.setText("");
                        mBinding.etContent.clearFocus();
                        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
                            Tools.hiddenKeyboard(StatusDetailActivity.this);
                        }
                        //TODO:刷新当前评论列表，显示刚提交的评论item

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.block_like:
                //welike没有点赞权限
                mBinding.ivLike.setSelected(!mBinding.ivLike.isSelected());
//                if (mBinding.ivLike.isSelected()) {
//                    HttpUtils.getInstance().getAttitudesService()
//                            .destroyLike(AccessTokenKeeper.getInstance().getAccessToken(), mBean.getIdstr())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Observer<AttitudeResultBean>() {
//                                @Override
//                                public void onCompleted() {
//                                    Log.i(TAG, "onCompleted");
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    Log.e(TAG, "onError:" + e.toString());
//                                }
//
//                                @Override
//                                public void onNext(AttitudeResultBean attitudeResultBean) {
//                                    EventBusUtils.sendEvent(ToastEvent.newToastEvent("取消点赞"));
//                                    AttitudesManager.getInstance().putAttitude(mBean.getIdstr(), 0);
//                                    mBinding.ivLike.setSelected(false);
//                                    mBinding.ivLike.startAnimation(AnimationUtils.loadAnimation(StatusDetailActivity.this, R.anim.anim_like_scale));
//                                    mBean.setAttitudes_count(mBean.getAttitudes_count() - 1);
//                                    mBinding.tvLikeCount.setText(String.valueOf(mBean.getAttitudes_count()));
//                                }
//                            });
//                } else {
//                    HttpUtils.getInstance().getAttitudesService()
//                            .createLike(AccessTokenKeeper.getInstance().getAccessToken(), mBean.getIdstr())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Observer<AttitudeResultBean>() {
//                                @Override
//                                public void onCompleted() {
//                                    Log.i(TAG, "onCompleted");
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    Log.e(TAG, "onError:" + e.toString());
//                                }
//
//                                @Override
//                                public void onNext(AttitudeResultBean attitudeResultBean) {
//                                    EventBusUtils.sendEvent(ToastEvent.newToastEvent("点赞"));
//                                    AttitudesManager.getInstance().putAttitude(mBean.getIdstr(), 1);
//                                    mBinding.ivLike.setSelected(true);
//                                    mBinding.ivLike.startAnimation(AnimationUtils.loadAnimation(StatusDetailActivity.this, R.anim.anim_like_scale));
//                                    mBean.setAttitudes_count(mBean.getAttitudes_count() + 1);
//                                    mBinding.tvLikeCount.setText(String.valueOf(mBean.getAttitudes_count()));
//                                }
//                            });
//                }
                break;
            case R.id.block_comment:
                mBinding.etContent.setFocusable(true);
                mBinding.etContent.setFocusableInTouchMode(true);
                mBinding.etContent.requestFocus();
                mBinding.etContent.requestFocusFromTouch();
                break;
            case R.id.block_repost:
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(StatusBean.INTENT_SERIALIZABLE, mBean);
                startActivity(new Intent(this, PostActivity.class)
                        .putExtras(mBundle)
                        .putExtra(PostActivity.INTENT_REPOST, true));
                break;
            case R.id.iv_commit:
                if (mBinding.ivCommit.isSelected()) {
                    createComment();
                }
                break;
            case R.id.iv_head:
                startActivity(new Intent(StatusDetailActivity.this, UserActivity.class)
                        .putExtra(UserActivity.INTENT_KEY_UID, mBean.getId()));
                break;
            case R.id.iv_more_option:
                //背景虚化
                setBgAlpha(0.5f);
                final Dialog dialog_more = new Dialog(StatusDetailActivity.this,R.style.DialogStatement);
                dialog_more.setContentView(LayoutInflater.from(StatusDetailActivity.this)
                        .inflate(R.layout.dialog_status_more, null));
                dialog_more.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //背景虚化恢复
                        setBgAlpha(1.0f);
                    }
                });
                WindowManager.LayoutParams param = dialog_more.getWindow().getAttributes(); // 获取对话框当前的参数值
                param.width = (int) (ScreenUtil.getScreenWidth(StatusDetailActivity.this) * 0.8); // 宽度设置为屏幕的
                dialog_more.getWindow().setAttributes(param);
                dialog_more.getWindow().setWindowAnimations(R.style.SelectPicStyle);//设置进出动画
                final TextView tv_favorite = (TextView) dialog_more.findViewById(R.id.tv_favorite);
                tv_favorite.setText((mBean.isFavorited() ? "取消" : "")+ "收藏");
                dialog_more.findViewById(R.id.tv_favorite).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBean.isFavorited()) {
                            HttpUtils.getInstance().getFavoriteService()
                                    .destroyFavorite(AccessTokenKeeper.getInstance().getAccessToken(), mBean.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<FavoriteBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            T.s(StatusDetailActivity.this,"操作失败");
                                            Log.e(TAG, "onError:"+e.toString());
                                        }

                                        @Override
                                        public void onNext(FavoriteBean resultBean) {
                                            T.s(StatusDetailActivity.this, "已取消收藏");
                                            mBean.setFavorited(false);
                                        }
                                    });
                        }else{
                            HttpUtils.getInstance().getFavoriteService()
                                    .createFavorite(AccessTokenKeeper.getInstance().getAccessToken(), mBean.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<FavoriteBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            T.s(StatusDetailActivity.this,"操作失败");
                                            Log.e(TAG, "onError:"+e.toString());
                                        }

                                        @Override
                                        public void onNext(FavoriteBean resultBean) {
                                            T.s(StatusDetailActivity.this, "已收藏");
                                            mBean.setFavorited(true);
                                        }
                                    });
                        }

                        dialog_more.dismiss();
                    }
                });
                UserBean user = mBean.getUser();
                String uid = null;
                if (user != null) {
                    uid = user.getId();
                }
                final TextView tvFollow = (TextView) dialog_more.findViewById(R.id.tv_follow);
                if (uid != null && uid.equals(AccessTokenKeeper.getInstance().getUid())) {
                    tvFollow.setVisibility(View.GONE);
                    dialog_more.findViewById(R.id.tv_destroy).setVisibility(View.VISIBLE);
                    dialog_more.findViewById(R.id.tv_destroy).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HttpUtils.getInstance().getWeiboService()
                                    .destroyStatus(AccessTokenKeeper.getInstance().getAccessToken(), mBean.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<StatusBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            T.s(StatusDetailActivity.this, "操作失败");
                                        }

                                        @Override
                                        public void onNext(StatusBean bean) {
                                            T.s(StatusDetailActivity.this, "删除微博成功");
                                            StatusDetailActivity.this.finish();
                                        }
                                    });
                            dialog_more.dismiss();
                        }
                    });
                }else{
                    //获取登录用户和此用户的关系
                    HttpUtils.getInstance().getFriendshipService()
                            .showFriendships(AccessTokenKeeper.getInstance().getUid(), uid)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<FriendshipsShowResultBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(FriendshipsShowResultBean friendshipsShowResultBean) {
                                    if (friendshipsShowResultBean.getSource().isFollowing()) {
                                        tvFollow.setText("已关注");
                                        tvFollow.setSelected(true);
                                    }else{
                                        tvFollow.setText("关注");
                                        tvFollow.setSelected(false);
                                    }
                                }
                            });
                    final String finalUid = uid;
                    tvFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tvFollow.isSelected()) {
                                HttpUtils.getInstance().getFriendshipService()
                                        .destroyFriendships(AccessTokenKeeper.getInstance().getAccessToken(), finalUid)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<FavoriteBean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                EventBusUtils.sendEvent(ToastEvent.newToastEvent("操作失败"));
                                            }

                                            @Override
                                            public void onNext(FavoriteBean favoriteBean) {
                                                EventBusUtils.sendEvent(ToastEvent.newToastEvent("取消关注"));
                                            }
                                        });
                            }else{//未关注
                                HttpUtils.getInstance().getFriendshipService()
                                        .createFriendships(AccessTokenKeeper.getInstance().getAccessToken(), finalUid)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<FavoriteBean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                EventBusUtils.sendEvent(ToastEvent.newToastEvent("操作失败"));
                                            }

                                            @Override
                                            public void onNext(FavoriteBean favoriteBean) {
                                                EventBusUtils.sendEvent(ToastEvent.newToastEvent("关注成功"));
                                            }
                                        });
                            }
                            dialog_more.dismiss();
                        }
                    });
                }
                dialog_more.show();
                break;
            case R.id.ll_retweeted:
                Bundle bundle = new Bundle();
                bundle.putSerializable(StatusBean.INTENT_SERIALIZABLE, mBean.getRetweeted_status());
                StatusDetailActivity.this.startActivity(
                        new Intent(StatusDetailActivity.this, StatusDetailActivity.class)
                                .putExtras(bundle));
                break;
            default:
                    break;
        }
    }

}
