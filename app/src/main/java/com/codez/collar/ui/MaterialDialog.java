package com.codez.collar.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codez.collar.R;


public class MaterialDialog {

    private Context mContext;
    private AlertDialog mAlertDialog;
    private MaterialDialog.Builder mBuilder;

    private int mTitleResId = -1;
    private CharSequence mTitle;
    private int mMessageResId = -1;
    private CharSequence mMessage;
    private int mTipResId = -1;
    private CharSequence mTip;
    private int pId = -1, nId = -1;
    private CharSequence pText, nText;
    private int mContentViewResId = -1;
    private View mContentView;
    private View mView;
    private boolean mCancel;
    private boolean mHasShow;
    private int mViewWidth;
    public final static int VIEW_NORMAL = 330;
    public final static int VIEW_NARROW = 236;

    public enum DialogStyle{
        TITLE_MESSAGE, TIP, CONTENT_VIEW, VIEW
    }
    private DialogStyle mStyle;
    private TextView tv_title;
    private TextView tv_message;
    private TextView tv_tip;
    private TextView btn_positive;
    private TextView btn_negative;
    private LinearLayout layout_content;
    private LinearLayout layout_view;
    private View.OnClickListener pListener, nListener;
    private DialogInterface.OnDismissListener mOnDismissListener;

    public MaterialDialog(Context context) {
        this.mContext = context;
        mHasShow = false;
        mCancel = true;
        mStyle = DialogStyle.TITLE_MESSAGE;
        mViewWidth = VIEW_NORMAL;
    }
    public MaterialDialog(Context context, DialogStyle style) {
        this(context);
        this.mStyle = style;
    }


    public void show() {
        if (!mHasShow) {
            mBuilder = new MaterialDialog.Builder();
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

    public MaterialDialog setViewWidth(int width){
        this.mViewWidth = width;
        return this;
    }

    public MaterialDialog setView(View view) {
        mStyle = DialogStyle.VIEW;
        mView = view;
        if (mBuilder != null) {
            mBuilder.setView(view);
        }
        return this;
    }
    public MaterialDialog setView(View view, DialogStyle style) {
        mStyle = style;
        mView = view;
        if (mBuilder != null) {
            mBuilder.setView(view);
        }
        return this;
    }

    public MaterialDialog setContentView(View view) {
        mStyle = DialogStyle.CONTENT_VIEW;
        mContentView = view;
        if (mBuilder != null) {
            mBuilder.setContentView(view);
        }
        return this;
    }

    public MaterialDialog setContentView(int layoutResId) {
        mStyle = DialogStyle.CONTENT_VIEW;
        mContentViewResId = layoutResId;
        mContentView = null;
        if (mBuilder != null) {
            mBuilder.setContentView(layoutResId);
        }
        return this;
    }

    public MaterialDialog setTitle(int resId) {
        mStyle = DialogStyle.TITLE_MESSAGE;
        mTitleResId = resId;
        if (mBuilder != null) {
            mBuilder.setTitle(resId);
        }
        return this;
    }


    public MaterialDialog setTitle(CharSequence title) {
        mStyle = DialogStyle.TITLE_MESSAGE;
        mTitle = title;
        if (mBuilder != null) {
            mBuilder.setTitle(title);
        }
        return this;
    }


    public MaterialDialog setMessage(int resId) {
        mStyle = DialogStyle.TITLE_MESSAGE;
        mMessageResId = resId;
        if (mBuilder != null) {
            mBuilder.setMessage(resId);
        }
        return this;
    }

    public MaterialDialog setMessage(CharSequence message) {
        mStyle = DialogStyle.TITLE_MESSAGE;
        mMessage = message;
        if (mBuilder != null) {
            mBuilder.setMessage(message);
        }
        return this;
    }

    public MaterialDialog setTip(int resId) {
        mStyle = DialogStyle.TIP;
        mTipResId = resId;
        if (mBuilder != null) {
            mBuilder.setTip(resId);
        }
        return this;
    }


    public MaterialDialog setTip(CharSequence tip) {
        mStyle = DialogStyle.TIP;
        mTip = tip;
        if (mBuilder != null) {
            mBuilder.setTip(tip);
        }
        return this;
    }

    public MaterialDialog setPositiveButton(int resId, final View.OnClickListener listener) {
        this.pId = resId;
        this.pListener = listener;
        return this;
    }

    public MaterialDialog setPositiveButton(CharSequence text, final View.OnClickListener listener) {
        this.pText = text;
        this.pListener = listener;
        return this;
    }

    public MaterialDialog setNegativeButton(int resId, final View.OnClickListener listener) {
        this.nId = resId;
        this.nListener = listener;
        return this;
    }

    public MaterialDialog setNegativeButton(CharSequence text, final View.OnClickListener listener) {
        this.nText = text;
        this.nListener = listener;
        return this;
    }

    public MaterialDialog setCanceledOnTouchOutside(boolean cancel) {
        this.mCancel = cancel;
        return this;
    }

    public MaterialDialog setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
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
            setCanceledOnTouchOutside(mCancel);

            View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_material, null);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            window.setContentView(contentView);

            tv_title = (TextView) window.findViewById(R.id.tv_title);
            tv_message = (TextView) window.findViewById(R.id.tv_message);
            tv_tip = (TextView) window.findViewById(R.id.tv_tip);
            btn_positive = (TextView) window.findViewById(R.id.btn_p);
            btn_negative = (TextView) window.findViewById(R.id.btn_n);
            layout_content = (LinearLayout) window.findViewById(R.id.ll_contentView);
            layout_view = (LinearLayout) window.findViewById(R.id.ll_view);
            tv_message.setMovementMethod(ScrollingMovementMethod.getInstance());
            tv_tip.setMovementMethod(ScrollingMovementMethod.getInstance());

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout_view.getLayoutParams();
            lp.width = dip2px((float)mViewWidth);
            layout_view.setLayoutParams(lp);

            switch (mStyle){
                case TITLE_MESSAGE:
                    window.findViewById(R.id.ll_title_content).setVisibility(View.VISIBLE);
                    if (mTitleResId != -1) {
                        setTitle(mTitleResId);
                    }
                    if (mTitle != null) {
                        setTitle(mTitle);
                    }
                    if (mMessageResId != -1) {
                        setMessage(mMessageResId);
                    }
                    if (mMessage != null) {
                        setMessage(mMessage);
                    }
                    break;
                case TIP:
                    window.findViewById(R.id.ll_tip).setVisibility(View.VISIBLE);
                    if (mTipResId != -1) {
                        setTip(mTipResId);
                    }
                    if (mTip != null) {
                        setTip(mTip);
                    }
                    break;
                case CONTENT_VIEW:
                    if (mContentView != null) {
                        this.setContentView(mContentView);
                    } else if (mContentViewResId != -1) {
                        this.setContentView(mContentViewResId);
                    }
                    break;
                case VIEW:
                    if (mView != null) {
                        layout_view.removeAllViews();
                        layout_view.addView(mView);
                    }
                    break;
            }

            if (pId != -1) {
                btn_positive.setVisibility(View.VISIBLE);
                btn_positive.setText(pId);
                btn_positive.setOnClickListener(pListener);
            }
            if (pText != null){
                btn_positive.setVisibility(View.VISIBLE);
                btn_positive.setText(pText);
                btn_positive.setOnClickListener(pListener);
            }
            if (nId != -1) {
                btn_negative.setVisibility(View.VISIBLE);
                btn_negative.setText(nId);
                btn_negative.setOnClickListener(nListener);
            }
            if (nText != null) {
                btn_negative.setVisibility(View.VISIBLE);
                btn_negative.setText(nText);
                btn_negative.setOnClickListener(nListener);
            }
            if (pId != -1 || nId != -1 || pText != null || nText != null) {
                window.findViewById(R.id.rl_btn).setVisibility(View.VISIBLE);
            }
            if (mOnDismissListener != null) {
                mAlertDialog.setOnDismissListener(mOnDismissListener);
            }
        }
        public void setView(View view){
            layout_view.removeAllViews();
            layout_view.addView(view);
        }

        public void setTitle(int resId) {
            tv_title.setText(resId);
        }

        public void setTitle(CharSequence title) {
            tv_title.setText(title);
        }

        public void setMessage(int resId) {
            tv_message.setText(resId);
        }

        public void setMessage(CharSequence message) {
            tv_message.setText(message);
        }

        public void setTip(int resId) {
            tv_tip.setText(resId);
        }

        public void setTip(CharSequence tip) {
            tv_tip.setText(tip);
        }

        public void setPositiveButton(String text, final View.OnClickListener listener) {
            btn_positive.setText(text);
            btn_positive.setOnClickListener(listener);
            btn_positive.setVisibility(View.VISIBLE);
        }

        public void setNegativeButton(String text, final View.OnClickListener listener) {
            btn_negative.setText(text);
            btn_negative.setOnClickListener(listener);
            btn_negative.setVisibility(View.VISIBLE);
        }

        public void setContentView(View contentView) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            contentView.setLayoutParams(layoutParams);
            if (contentView instanceof ListView) {
                setListViewHeightBasedOnChildren((ListView) contentView);
            }
            LinearLayout linearLayout = (LinearLayout) window.findViewById(R.id.ll_contentView);
            if (linearLayout != null) {
                linearLayout.removeAllViews();
                linearLayout.addView(contentView);
            }
            for (int i = 0; i < (linearLayout != null ? linearLayout.getChildCount() : 0); i++) {
                if (linearLayout.getChildAt(i) instanceof AutoCompleteTextView) {
                    AutoCompleteTextView autoCompleteTextView
                            = (AutoCompleteTextView) linearLayout.getChildAt(i);
                    autoCompleteTextView.setFocusable(true);
                    autoCompleteTextView.requestFocus();
                    autoCompleteTextView.setFocusableInTouchMode(true);
                }
            }
        }

        public void setContentView(int layoutResId) {
            layout_content.removeAllViews();
            LayoutInflater.from(layout_content.getContext())
                    .inflate(layoutResId, layout_content);
        }

        public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            mAlertDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            mAlertDialog.setCancelable(canceledOnTouchOutside);
        }

        private void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                return;
            }

            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }
    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}