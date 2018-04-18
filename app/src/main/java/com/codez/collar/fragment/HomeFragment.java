package com.codez.collar.fragment;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.codez.collar.R;
import com.codez.collar.activity.SearchActivity;
import com.codez.collar.activity.UserActivity;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.FragmentHomeBinding;
import com.codez.collar.event.GroupChangedEvent;
import com.codez.collar.event.ToastEvent;
import com.codez.collar.event.TranslucentMaskDisplayEvent;
import com.codez.collar.event.UpdateUserInfoEvent;
import com.codez.collar.manager.UserManager;
import com.codez.collar.ui.AppDialog;
import com.codez.collar.ui.GroupPopupWindow;
import com.codez.collar.ui.HomeTitleTextView;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.T;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    private boolean isLeft;
    private GroupPopupWindow groupWindow = null;
    private GroupChangedEvent mGroupChangedEvent = null;
    private String mCurGroupName;
    public static final String STATUS_GROUP_ALL = "全部";
    public static final int REQUEST_CODE_ZXING = 2;

    private Fragment[] fragments;
    @Override
    public int setContent() {
        return R.layout.fragment_home;
    }


    @Override
    public void initView(View root) {
        EventBusUtils.register(this);

        mCurGroupName = STATUS_GROUP_ALL;

        fragments = new Fragment[]{new StatusListFragment().newInstance(AccessTokenKeeper.getInstance().getUid(),STATUS_GROUP_ALL, StatusListFragment.VALUE_HOME),
        new StatusListFragment().newInstance(null,null, StatusListFragment.VALUE_PUBLIC)};

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_statuses,fragments[0])
                .show(fragments[0]).commit();
        isLeft = true;

        mBinding.tvLeft.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
        mBinding.tvRight.changeState(HomeTitleTextView.STATE_UNSELECTED);
        mBinding.tvLeft.setOnClickListener(this);
        mBinding.tvRight.setOnClickListener(this);
        mBinding.ivSearch.setOnClickListener(this);
        mBinding.ivScan.setOnClickListener(this);
        mBinding.ivUser.setOnClickListener(this);
        initData();

    }

    private void initData(){
        refreshUserInfo(UserManager.getInstance().getUserMe());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                if (isLeft){
                    if (groupWindow == null){
                        groupWindow = new GroupPopupWindow(getContext(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    groupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            mBinding.tvLeft.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
                            EventBusUtils.sendEvent(new TranslucentMaskDisplayEvent(false));
                        }
                    });
                    int[] location = new int[2];
                    mBinding.appbar.getLocationInWindow(location);
                    if (mBinding.tvLeft.getState() == HomeTitleTextView.STATE_SELECTED_CLOSE) {
                        mBinding.tvLeft.changeState(HomeTitleTextView.STATE_SELECTED_OPEN);
                        EventBusUtils.sendEvent(new TranslucentMaskDisplayEvent(true));
                        groupWindow.showAtLocation(mBinding.appbar, Gravity.NO_GRAVITY, 0, location[1]+mBinding.appbar.getHeight());
                    }else{
                        mBinding.tvLeft.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
                        groupWindow.dismiss();
                    }
                } else {
                    mBinding.tvLeft.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
                    mBinding.tvRight.changeState(HomeTitleTextView.STATE_UNSELECTED);
                    isLeft = true;
                    FragmentTransaction trx = getActivity().getSupportFragmentManager().beginTransaction();
                    trx.hide(fragments[1]);
                    if (!fragments[0].isAdded()) {
                        trx.add(R.id.container_statuses, fragments[0]);
                    }
                    trx.show(fragments[0]).commit();
                }
                break;
            case R.id.tv_right:
                if (isLeft){
                    mBinding.tvLeft.changeState(HomeTitleTextView.STATE_UNSELECTED);
                    mBinding.tvRight.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
                    isLeft = false;
                    FragmentTransaction trx = getActivity().getSupportFragmentManager().beginTransaction();
                    trx.hide(fragments[0]);
                    if (!fragments[1].isAdded()) {
                        trx.add(R.id.container_statuses, fragments[1]);
                    }
                    trx.show(fragments[1]).commit();
                }else{
                    if (mBinding.tvRight.getState() == HomeTitleTextView.STATE_SELECTED_CLOSE) {
                        mBinding.tvRight.changeState(HomeTitleTextView.STATE_SELECTED_OPEN);
                        //TODO:popupWindow open
                        T.s(getContext(),"right open");
                    }else{
                        mBinding.tvRight.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
                        //TODO:popWindow close
                        T.s(getContext(),"right close");
                    }
                }
                break;
            case R.id.iv_user:
                startActivity(new Intent(getActivity(), UserActivity.class)
                        .putExtra(UserActivity.INTENT_KEY_UID, AccessTokenKeeper.getInstance().getUid()),
                        ActivityOptions.makeSceneTransitionAnimation(getActivity(),mBinding.ivUser,"shareAvatar").toBundle());
                break;
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.iv_scan:
                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_CODE_ZXING);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }
    private void refreshUserInfo(final UserBean user) {
        if (user == null) {
            return;
        }
        mBinding.setUser(user);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupChangedEvent(GroupChangedEvent event) {
        mGroupChangedEvent = event;
        mBinding.tvLeft.setText(event.getName());
        mBinding.tvLeft.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
        groupWindow.dismiss();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateUserInfoEvent(UpdateUserInfoEvent event) {
        refreshUserInfo(UserManager.getInstance().getUserMe());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ZXING){
            //处理扫描结果（在界面上显示）
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.i(TAG, "result:" + result);
                    final AppDialog dialog = new AppDialog(getActivity());
                    dialog.setTitle("扫描结果")
                            .setMessage(result)
                            .setNegativeButton("继续扫描", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_CODE_ZXING);
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("复制", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                    cm.setPrimaryClip(ClipData.newPlainText("Label", result));
                                    EventBusUtils.sendEvent(ToastEvent.newToastEvent("该段文字已复制到剪贴板"));
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    final AppDialog dialog = new AppDialog(getActivity());
                    dialog.setTitle("扫描结果")
                            .setMessage("扫描失败")
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("继续扫描", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_CODE_ZXING);
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
