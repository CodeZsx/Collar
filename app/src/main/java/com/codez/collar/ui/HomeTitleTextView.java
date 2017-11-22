package com.codez.collar.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;

import com.codez.collar.R;
import com.codez.collar.utils.DensityUtil;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;
import skin.support.widget.SkinCompatTextHelper;

/**
 * Created by codez on 2017/11/20.
 * Description:
 */

public class HomeTitleTextView extends android.support.v7.widget.AppCompatTextView implements SkinCompatSupportable{

    //skin
    private SkinCompatTextHelper mTextHelper;
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    private Context mContext;
    private int mState;
    private final int DRAWABLE_RIGHT_SIZE = 8;

    public HomeTitleTextView(Context context) {
        this(context,null);
    }

    public HomeTitleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public HomeTitleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
        mTextHelper = SkinCompatTextHelper.create(this);
        mTextHelper.loadFromAttributes(attrs, defStyleAttr);

        this.mContext = context;
        mState = 1;
        changeState(mState);

    }
    public static final int STATE_SELECTED_CLOSE = 1;
    public static final int STATE_SELECTED_OPEN = 2;
    public static final int STATE_UNSELECTED = 3;

    public void changeState(int state) {
        Drawable arrow;
        switch (state) {
            case STATE_SELECTED_CLOSE:
                mState = STATE_SELECTED_CLOSE;
                setTextAppearance(R.style.HomeTitleColorSel);
//                setTextColor(mContext.getResources().getColor(R.color.colorTextLevel1));
                arrow = mContext.getResources().getDrawable(R.drawable.ic_arrow_fill_down);
                arrow.setBounds(0, 0, DensityUtil.dp2px(mContext, DRAWABLE_RIGHT_SIZE),
                        DensityUtil.dp2px(mContext, DRAWABLE_RIGHT_SIZE));
                setCompoundDrawables(null, null, arrow, null);
                break;
            case STATE_SELECTED_OPEN:
                mState = STATE_SELECTED_OPEN;
                setTextAppearance(R.style.HomeTitleColorSel);
//                setTextColor(mContext.getResources().getColor(R.color.colorTextLevel1));
                arrow = mContext.getResources().getDrawable(R.drawable.ic_arrow_fill_up);
                arrow.setBounds(0, 0, DensityUtil.dp2px(mContext, DRAWABLE_RIGHT_SIZE),
                        DensityUtil.dp2px(mContext, DRAWABLE_RIGHT_SIZE));
                setCompoundDrawables(null, null, arrow, null);
                break;
            case STATE_UNSELECTED:
                mState = STATE_UNSELECTED;
                setTextAppearance(R.style.HomeTitleColorNor);
//                setTextColor(mContext.getResources().getColor(R.color.colorTextLevel2));
                setCompoundDrawables(null, null, null, null);
                break;
        }
    }

    public int getState() {
        return mState;
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
        if (mTextHelper != null) {
            mTextHelper.applySkin();
        }
    }

    @Override
    public void setTextAppearance(@StyleRes int resId) {
        setTextAppearance(getContext(), resId);
    }

    @Override
    public void setTextAppearance(Context context, @StyleRes int resId) {
        super.setTextAppearance(context, resId);
        if (mTextHelper != null) {
            mTextHelper.onSetTextAppearance(context, resId);
        }
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resid) {
        super.setBackgroundResource(resid);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resid);
        }
    }
}
