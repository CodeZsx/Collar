package com.codez.collar.manager;

import android.util.Log;

import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.bean.UserBean;
import com.codez.collar.event.UpdateUserInfoEvent;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.worker.IBaseHandler;
import com.codez.collar.worker.Worker;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by codez on 2018/2/1.
 * Description:
 */

public class UserManager {
    private static final String TAG = "UserManager";

    private Worker worker;

    public UserManager() {
        worker = new Worker();
        worker.initilize(TAG+" work!");
    }

    static class Loader {
        static final UserManager INSTANCE = new UserManager();
    }

    public static UserManager getInstance() {
        return Loader.INSTANCE;
    }

    public void addHandler(IBaseHandler handler) {
        worker.addHandler(handler);
    }


    private UserBean userMe;

    public void initUserInfo(){
        Log.i(TAG, "initUserInfo");
        if (worker == null) {
            worker = new Worker();
            worker.initilize(TAG + " work!");
        }
        HttpUtils.getInstance().getUserService()
                .getUserInfo(AccessTokenKeeper.getInstance().getUid(), null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w(TAG,"onError:"+e.toString());
                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        Log.i(TAG,"onNext");
                        userMe = userBean;
                        sendUpdateUserInfoEvent();
                    }
                });
    }

    private void sendUpdateUserInfoEvent() {
        EventBusUtils.sendEvent(new UpdateUserInfoEvent());
    }

    private List<UserBean> mSearchUserTempList = new ArrayList<>();
    private String mSearchWord = null;

    public List<UserBean> getSearchUserResult(String queryWord) {
        if (mSearchWord != null && mSearchWord.equals(queryWord)) {
            return mSearchUserTempList;
        }else{
            return null;
        }
    }
    public UserBean getUserMe() {
        if (userMe == null){
            Log.w(TAG,"userme is null");
            initUserInfo();
        }
        return userMe;
    }

    public void setUserMe(UserBean userMe) {
        this.userMe = userMe;
    }
}
