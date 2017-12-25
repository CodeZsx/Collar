package com.codez.collar.ui.emojitextview;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.codez.collar.R;


/**
 * Created by codez on 17/11/24.
 */
public class WeiBoContentClickableSpan extends ClickableSpan {

    private Context mContext;
    private int color;

    public WeiBoContentClickableSpan(Context context) {
        mContext = context;
    }
    public WeiBoContentClickableSpan(Context context,int color) {
        mContext = context;
        this.color = color;
    }

    @Override
    public void onClick(View widget) {
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if (color != 0) {
            ds.setColor(color);
        }else{
            ds.setColor(mContext.getResources().getColor(R.color.colorTextStatusLink));
        }
        ds.setUnderlineText(false);
    }


}
