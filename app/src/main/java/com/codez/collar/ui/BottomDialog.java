package com.codez.collar.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.codez.collar.R;


public class BottomDialog {

    private Context mContext;
    private AlertDialog mAlertDialog;
    private BottomDialog.Builder mBuilder;

    private View mView;
    private boolean mCancel;
    private boolean mHasShow;

    private LinearLayout layout_view;
    private DialogInterface.OnDismissListener mOnDismissListener;

    public BottomDialog(Context context) {
        this.mContext = context;
        mHasShow = false;
        mCancel = true;
    }

    public void show() {
        if (!mHasShow) {
            mBuilder = new BottomDialog.Builder();
        } else {
            mAlertDialog.show();
        }
        mHasShow = true;
    }

    public boolean isShowing(){
        return mHasShow;
    }

    public void dismiss() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    public BottomDialog setView(View view) {
        mView = view;
        if (mBuilder != null) {
            mBuilder.setView(view);
        }
        return this;
    }

    public BottomDialog setCanceledOnTouchOutside(boolean cancel) {
        this.mCancel = cancel;
        return this;
    }

    public BottomDialog setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        return this;
    }

    private class Builder {

        private Window window;

        private Builder() {
            mAlertDialog = new AlertDialog.Builder(mContext).create();
            mAlertDialog.show();
            window = mAlertDialog.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
//            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            setCanceledOnTouchOutside(mCancel);

            View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_bottom, null);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            window.setContentView(contentView);

            layout_view = (LinearLayout) window.findViewById(R.id.ll_view);

            if (mView != null) {
                layout_view.removeAllViews();
                layout_view.addView(mView);
            }

            if (mOnDismissListener != null) {
                mAlertDialog.setOnDismissListener(mOnDismissListener);
            }
        }

        public void setView(View view) {
            layout_view.removeAllViews();
            layout_view.addView(view);
        }

        public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            mAlertDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            mAlertDialog.setCancelable(canceledOnTouchOutside);
        }
    }
    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}