package com.codez.collar.event;

/**
 * Created by codez on 2018/4/7.
 * Description:
 */

public class NightModeChangedEvent {
    private boolean night;

    public NightModeChangedEvent(boolean night) {
        this.night = night;
    }

    public boolean isNight() {
        return night;
    }

    public void setNight(boolean night) {
        this.night = night;
    }
}
