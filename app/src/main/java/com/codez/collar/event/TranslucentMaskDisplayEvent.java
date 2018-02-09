package com.codez.collar.event;

/**
 * Created by codez on 2018/2/9.
 * Description:
 */

public class TranslucentMaskDisplayEvent {
    private boolean isDisplay;

    public TranslucentMaskDisplayEvent(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean display) {
        isDisplay = display;
    }
}
