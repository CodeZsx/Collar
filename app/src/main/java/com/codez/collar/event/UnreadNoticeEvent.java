package com.codez.collar.event;

/**
 * Created by codez on 2017/12/25.
 * Description:
 */

public class UnreadNoticeEvent {
    private int navigationId;
    private int count;

    public UnreadNoticeEvent(int navigationId, int count) {
        this.navigationId = navigationId;
        this.count = count;
    }

    public int getNavigationId() {
        return navigationId;
    }

    public void setNavigationId(int navigationId) {
        this.navigationId = navigationId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
