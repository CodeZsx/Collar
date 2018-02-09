package com.codez.collar.manager;

import android.util.Log;

import com.codez.collar.bean.Group;
import com.codez.collar.bean.GroupsResult;
import com.codez.collar.event.GroupsUpdateEvent;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.worker.GroupsUpdateHandler;
import com.codez.collar.worker.IBaseHandler;
import com.codez.collar.worker.Worker;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by codez on 2018/2/1.
 * Description:
 */

public class GroupManager {
    private static final String TAG = "GroupManager";

    private Worker worker;
    private List<Group> mGroups;

    public GroupManager() {
        worker = new Worker();
        worker.initilize(TAG+" work!");
    }

    static class Loader {
        static final GroupManager INSTANCE = new GroupManager();
    }

    public static GroupManager getInstance() {
        return Loader.INSTANCE;
    }

    public void addHandler(IBaseHandler handler) {
        worker.addHandler(handler);
    }

    public void initGroups() {
        Log.i(TAG, "initGroups");
        HttpUtils.getInstance().getFriendshipService()
                .getGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GroupsResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w(TAG, "onError:" + e.toString());
                    }

                    @Override
                    public void onNext(GroupsResult groupsResult) {
                        mGroups = groupsResult.getLists();
                        EventBusUtils.sendEvent(new GroupsUpdateEvent());
                    }
                });
    }

    public List<Group> getGroups() {
        if (mGroups == null) {
            if (worker != null) {
                worker.addHandler(new GroupsUpdateHandler());
            }
        }
        return mGroups;
    }

}
