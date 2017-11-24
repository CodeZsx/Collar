package com.codez.collar.ui.emojitextview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by codez on 17/11/24.
 */
public class EmojiTextView extends TextView {


    private final Context mContext;

    public EmojiTextView(Context context) {
        super(context, null);
        mContext = context;
    }

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

}
