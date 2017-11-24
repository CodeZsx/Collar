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

    public WeiBoContentClickableSpan(Context context) {
        mContext = context;
    }

    @Override
    public void onClick(View widget) {
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mContext.getResources().getColor(R.color.colorTextStatusLink));
        ds.setUnderlineText(false);
    }


}
