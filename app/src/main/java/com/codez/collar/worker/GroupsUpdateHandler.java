package com.codez.collar.worker;

import com.codez.collar.manager.GroupManager;

/**
 * Created by codez on 2018/2/8.
 * Description:
 */

public class GroupsUpdateHandler implements IBaseHandler{
    private static final String TAG = "GroupsUpdateHandler";
    @Override
    public void execute() {
        GroupManager.getInstance().getGroups();
    }
}
