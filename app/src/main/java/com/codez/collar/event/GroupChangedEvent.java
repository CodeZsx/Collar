package com.codez.collar.event;

/**
 * Created by codez on 2018/2/9.
 * Description: 选定群组发生改变，通知StatusListFragment刷新数据
 */

public class GroupChangedEvent {
    public static final int TYPE_ALL = 0;
    public static final int TYPE_GROUP = 1;
    private int type;
    private String name;
    private String id;

    public GroupChangedEvent(int type, String name, String id) {
        this.type = type;
        this.name = name;
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
