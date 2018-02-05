package com.codez.collar.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.codez.collar.Config;
import com.codez.collar.R;
import com.codez.collar.utils.DensityUtil;

import skin.support.widget.SkinCompatTextHelper;
import skin.support.widget.SkinCompatTextView;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

/**
 * Created by codez on 2017/11/20.
 * Description:
 */

public class HomeTitleTextView extends SkinCompatTextView {

    //skin
    private SkinCompatTextHelper mTextHelper;
    private int mArrowResId = INVALID_ID;

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
        switch (state) {
            case STATE_SELECTED_CLOSE:
                mState = STATE_SELECTED_CLOSE;
                setTextAppearance(R.style.HomeTitleColorSel);
                if (Config.getCachedTheme(getContext()).equals("a")){
                    mArrowResId = R.drawable.ic_arrow_fill_down_a;
                }else{
                    mArrowResId = R.drawable.ic_arrow_fill_down;
                }
                Drawable arrow = mContext.getResources().getDrawable(mArrowResId);
                arrow.setBounds(0, 0, DensityUtil.dp2px(mContext, DRAWABLE_RIGHT_SIZE),
                        DensityUtil.dp2px(mContext, DRAWABLE_RIGHT_SIZE));
                setCompoundDrawables(null, null, arrow, null);
//                applyArrowResource();
                break;
            case STATE_SELECTED_OPEN:
                mState = STATE_SELECTED_OPEN;
                setTextAppearance(R.style.HomeTitleColorSel);
                if (Config.getCachedTheme(getContext()).equals("a")){
                    mArrowResId = R.drawable.ic_arrow_fill_up_a;
                }else{
                    mArrowResId = R.drawable.ic_arrow_fill_up;
                }
                Drawable arrow1 = mContext.getResources().getDrawable(mArrowResId);
                arrow1.setBounds(0, 0, DensityUtil.dp2px(mContext, DRAWABLE_RIGHT_SIZE),
                        DensityUtil.dp2px(mContext, DRAWABLE_RIGHT_SIZE));
                setCompoundDrawables(null, null, arrow1, null);
//                applyArrowResource();
                break;
            case STATE_UNSELECTED:
                mState = STATE_UNSELECTED;
                setTextAppearance(R.style.HomeTitleColorNor);
                setCompoundDrawables(null, null, null, null);
                break;
        }
    }

    public int getState() {
        return mState;
    }

    private void applyArrowResource() {
//        Log.i("codez", "before:" + mArrowResId);
//        mArrowResId = SkinCompatHelper.checkResourceId(mArrowResId);
//        Log.i("codez", "after:" + mArrowResId);
//        if (mArrowResId != INVALID_ID) {
//            Drawable arrow = mContext.getResources().getDrawable(mArrowResId);
//            arrow.setBounds(0, 0, DensityUtil.dp2px(mContext, DRAWABLE_RIGHT_SIZE),
//                    DensityUtil.dp2px(mContext, DRAWABLE_RIGHT_SIZE));
//            setCompoundDrawables(null, null, arrow, null);
//        }
    }

//    @Override
//    public void applySkin() {
//        super.applySkin();
//        applyArrowResource();
//    }
}
