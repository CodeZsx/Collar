package com.codez.collar.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.codez.collar.R;
import com.codez.collar.utils.DensityUtil;

import java.util.ArrayList;

/**
 * Created by codez on 2017/12/21.
 * Description:
 */

public class IndicatorView extends LinearLayout{
    private Context mContext;
    private ArrayList<ImageView> mImageViews;
    private Bitmap bmpSelect;
    private Bitmap bmpNomal;
    private static final int HEIGHT = 16;
    private int mMaxHeight;
    private AnimatorSet mPlayToAnimatorSet;
    private AnimatorSet mPlayByInAnimatorSet;
    private AnimatorSet mPlayByOutAnimatorSet;


    public IndicatorView(Context context) {
        this(context,null);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOrientation(HORIZONTAL);
        mMaxHeight = DensityUtil.dp2px(mContext, HEIGHT);
        bmpSelect = BitmapFactory.decodeResource(getResources(), R.drawable.ic_indicator_point_select);
        bmpNomal = BitmapFactory.decodeResource(getResources(), R.drawable.ic_indicator_point_nomal);
    }

    public void init(int count) {
        mImageViews = new ArrayList<>();
        removeAllViews();
        for (int i = 0; i < count; i++) {
            RelativeLayout rl = new RelativeLayout(mContext);
            LayoutParams llParams = new LinearLayout.LayoutParams(mMaxHeight, mMaxHeight);
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            ImageView iv = new ImageView(mContext);
            if (i == 0) {
                iv.setImageBitmap(bmpSelect);
                rl.addView(iv, rlParams);
            }else{
                iv.setImageBitmap(bmpNomal);
                rl.addView(iv, rlParams);
            }
            addView(rl, llParams);
            mImageViews.add(iv);
        }
    }

    public void setIndicatorCount(int count) {
        if (mImageViews == null || count > mImageViews.size()) {
            return;
        }
        for (int i = 0; i  < mImageViews.size();i++) {
            if (i >= count) {
                mImageViews.get(i).setVisibility(GONE);
                ((View)mImageViews.get(i).getParent()).setVisibility(GONE);
            }else{
                mImageViews.get(i).setVisibility(VISIBLE);
                ((View) mImageViews.get(i).getParent()).setVisibility(VISIBLE);
            }
        }
    }

    //指示器圆点滑动到对应position时，圆点选中状态设置以及动画设置
    public void playTo(int position) {
        //取消上一次的选中
        for (ImageView iv : mImageViews) {
            iv.setImageBitmap(bmpNomal);
        }
        mImageViews.get(position).setImageBitmap(bmpSelect);
        ImageView ivTarget = mImageViews.get(position);
        ObjectAnimator animX = ObjectAnimator.ofFloat(ivTarget, "scaleX", 0.25f, 1.0f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(ivTarget, "scaleY", 0.25f, 1.0f);
        //若正在执行该动画，则停止重置
        if (mPlayToAnimatorSet != null && mPlayToAnimatorSet.isRunning()) {
            mPlayToAnimatorSet.cancel();
            mPlayToAnimatorSet = null;
        }
        mPlayToAnimatorSet = new AnimatorSet();
        mPlayToAnimatorSet.play(animX).with(animY);
        mPlayToAnimatorSet.setDuration(100);
        mPlayToAnimatorSet.start();
    }

    public void playBy(int startPosition, int nextPosition) {
        boolean isShowInAnimOnly = false;
        if (startPosition < 0 || nextPosition < 0 || nextPosition == startPosition) {
            startPosition = nextPosition = 0;
        }
        if (startPosition < 0) {
            isShowInAnimOnly = true;
            startPosition = nextPosition = 0;
        }
        final ImageView ivStart = mImageViews.get(startPosition);
        final ImageView ivNext = mImageViews.get(nextPosition);

        ObjectAnimator animX = ObjectAnimator.ofFloat(ivStart, "scaleX", 1.0f, 0.25f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(ivNext, "scaleY", 1.0f, 0.25f);

        //若正在执行该动画，则停止重置
        if (mPlayByInAnimatorSet != null && mPlayByInAnimatorSet.isRunning()) {
            mPlayByInAnimatorSet.cancel();
            mPlayByInAnimatorSet = null;
        }
        mPlayByOutAnimatorSet = new AnimatorSet();
        mPlayByOutAnimatorSet.play(animX).with(animY);
        mPlayByOutAnimatorSet.setDuration(100);

        ObjectAnimator animInX = ObjectAnimator.ofFloat(ivNext, "scaleX", 0.25f, 1.0f);
        ObjectAnimator animInY = ObjectAnimator.ofFloat(ivNext, "scaleY", 0.25f, 1.0f);
        mPlayByInAnimatorSet = new AnimatorSet();
        mPlayByInAnimatorSet.play(animInX).with(animInY);

        if (isShowInAnimOnly) {
            mPlayByInAnimatorSet.start();
            return;
        }
        animX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ivStart.setImageBitmap(bmpNomal);
                ObjectAnimator animFillX = ObjectAnimator.ofFloat(ivStart, "scaleX", 1.0f);
                ObjectAnimator animFillY = ObjectAnimator.ofFloat(ivStart, "scaleY", 1.0f);
                AnimatorSet mFillAnimatorSet = new AnimatorSet();
                mFillAnimatorSet.play(animFillX).with(animFillY);
                mFillAnimatorSet.start();
                ivNext.setImageBitmap(bmpSelect);
                mPlayByInAnimatorSet.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mPlayByOutAnimatorSet.start();

    }
}
