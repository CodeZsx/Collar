package com.codez.collar.ui;

import android.content.Context;
import android.view.View;


/**
 * 封装一下Dialog类，方便以后替换（只需改变此类内部实现即可）
 * Created by CodeZ
 */
public class AppDialog extends MaterialDialog {
    //记录是不是替换view的标志位
    private boolean isStardard;
    public final static int VIEW_NORMAL = MaterialDialog.VIEW_NORMAL;
    public final static int VIEW_NARROW = MaterialDialog.VIEW_NARROW;
    public AppDialog(Context context) {
        super(context);
        super.setViewWidth(VIEW_NORMAL);
        isStardard = true;
    }

    public AppDialog setTitle(String title){
        super.setTitle(title);
        return this;
    }
    public AppDialog setTitle(int resId){
        super.setTitle(resId);
        return this;
    }

    public AppDialog setMessage(int resId){
        super.setMessage(resId);
        return this;
    }

    public AppDialog setMessage(CharSequence message) {
        super.setMessage(message);
        return this;
    }

    public AppDialog setPositiveButton(String btnConent, View.OnClickListener listener){
        super.setPositiveButton(btnConent,listener);
        return this;
    }

    public AppDialog setNegativeButton(String btnConent, View.OnClickListener listener){
        super.setNegativeButton(btnConent,listener);
        return this;
    }
    public AppDialog setPositiveButton(int resId, View.OnClickListener listener){
        super.setPositiveButton(resId,listener);
        return this;
    }

    public AppDialog setNegativeButton(int resId, View.OnClickListener listener){
        super.setNegativeButton(resId,listener);
        return this;
    }

    public AppDialog setContentView(View view) {
        super.setContentView(view);
        return this;
    }

    public AppDialog setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public AppDialog setView(View view) {
        isStardard = false;
        super.setView(view);
        return this;
    }

    public AppDialog setViewWidth(int width){
        super.setViewWidth(width);
        return this;
    }

    public boolean isNeedReset(){
        return !isStardard;
    }

    public void show(){
        super.show();
    }

    public void dismiss(){
        super.dismiss();
    }
}
