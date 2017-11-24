package com.codez.collar.ui.emojitextview;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.view.View;

public abstract class ImageClickableSpan extends ImageSpan {
    public ImageClickableSpan(Drawable b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public abstract void onClick(View view);
}