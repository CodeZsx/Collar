package com.codez.collar.bean;

import java.util.List;

/**
 * Created by codez on 2018/2/8.
 * Description:
 */

public class GroupsResult {
    private List<Group> lists;
    private int total_number;

    public List<Group> getLists() {
        return lists;
    }

    public void setLists(List<Group> lists) {
        this.lists = lists;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }
}
