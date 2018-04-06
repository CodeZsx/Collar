package com.codez.collar.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.codez.collar.R;
import com.codez.collar.activity.PostActivity;
import com.codez.collar.activity.SearchActivity;
import com.codez.collar.activity.UserActivity;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.databinding.FragmentHomeBinding;
import com.codez.collar.event.GroupChangedEvent;
import com.codez.collar.event.TranslucentMaskDisplayEvent;
import com.codez.collar.ui.GroupPopupWindow;
import com.codez.collar.ui.HomeTitleTextView;
import com.codez.collar.ui.zxing.CaptureActivity;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.T;
import com.codez.collar.utils.TimeUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    private boolean isLeft;
    private GroupPopupWindow groupWindow = null;
    private GroupChangedEvent mGroupChangedEvent = null;
    private String mCurGroupName;
    public static final String STATUS_GROUP_ALL = "全部";

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
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                if (isLeft){
                    if (groupWindow == null){
                        groupWindow = new GroupPopupWindow(getContext(), ViewGroup.LayoutParams.MATCH_PARENT, 600);
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
                new IntentIntegrator(getActivity())
                        .setCaptureActivity(CaptureActivity.class)
                        .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                        .setPrompt("请对准二维码")// 设置提示语
                        .setCameraId(0)// 选择摄像头,可使用前置或者后置
                        .setBeepEnabled(false)// 是否开启声音,扫完码之后会"哔"的一声
                        .setBarcodeImageEnabled(true)// 扫完码之后生成二维码的图片
                        .initiateScan();// 初始化扫码
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupChangedEvent(GroupChangedEvent event) {
        Log.i(TAG, "onGroupChangedEvent");
        mGroupChangedEvent = event;
        mBinding.tvLeft.setText(event.getName());
        mBinding.tvLeft.changeState(HomeTitleTextView.STATE_SELECTED_CLOSE);
        groupWindow.dismiss();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
