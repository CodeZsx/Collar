package com.codez.collar.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import skin.support.widget.SkinCompatSupportable;

/**
 * Created by codez on 2017/12/25.
 * Description:
 */

public class ThemeToolbar extends Toolbar implements SkinCompatSupportable{
    public ThemeToolbar(Context context) {
        super(context);
    }

    public ThemeToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemeToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void applySkin() {

    }
}
