package com.codez.collar.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.codez.collar.R;
import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.databinding.ActivityPostBinding;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.L;
import com.codez.collar.utils.T;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PostActivity extends BaseActivity<ActivityPostBinding> implements View.OnClickListener{

    public static final String INTENT_REPOST = "repost";
    private static final int STATUS_MAX_LENGTH = 140;

    private boolean isRepost;

    @Override
    public int setContent() {
        return R.layout.activity_post;
    }

    @Override
    public void initView(){
        isRepost = getIntent().getBooleanExtra(INTENT_REPOST, false);
        if (isRepost) {
            setToolbarTitle(mBinding.toolbar, "转发微博");
        }else{
            setToolbarTitle(mBinding.toolbar, "发布微博");
        }

        mBinding.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(s.toString())) {
                    mBinding.ivCommit.setSelected(false);
                }else{
                    mBinding.ivCommit.setSelected(true);
                }
                mBinding.tvStatusLength.setText((STATUS_MAX_LENGTH - s.length())+"");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mBinding.ivAlbum.setOnClickListener(this);
        mBinding.ivEmoj.setOnClickListener(this);
        mBinding.ivAt.setOnClickListener(this);
        mBinding.ivTopic.setOnClickListener(this);
        mBinding.ivCommit.setOnClickListener(this);
        mBinding.tvAddress.setOnClickListener(this);

    }

    private void postStatus() {
        //对微博内容长度进行判断，超过STATUS_MAX_LENGTH（140）则不予发送
        if (mBinding.etContent.length() > STATUS_MAX_LENGTH) {
            T.s(this,"微博无法发送，内容长度超过140个字符！");
            return;
        }
        //发送文字微博
        HttpUtils.getInstance().getWeiboService(this)
                .postTextStatus(AccessTokenKeeper.getAccessToken(this), mBinding.etContent.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusBean>() {
                    @Override
                    public void onCompleted() {
                        L.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError:"+e.toString());
                    }

                    @Override
                    public void onNext(StatusBean bean) {
                        T.s(PostActivity.this, "微博发送成功");
                        PostActivity.this.finish();
                    }
                });
    }

    private void getLocation() {
        Observable.just("")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        startLocation();
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_LOCATION_RESULT) {
                if (mLocationClient != null) {
                    mLocationClient.onDestroy();
                }
                mBinding.tvAddress.setText(msg.obj.toString());
                mBinding.tvAddress.setSelected(true);
                L.e("地址："+msg.obj);
            }

        }
    };
    private static final int MESSAGE_LOCATION_RESULT = 1;
    AMapLocationClient mLocationClient = null;
    private void startLocation() {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE);
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        Message message = new Message();
                        message.what = MESSAGE_LOCATION_RESULT;
                        message.obj = aMapLocation.getAddress();
                        mHandler.sendMessage(message);
                    }else{
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置高精度定位模式：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（WIFI和基站定位）
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//        //设置仅用设备定位模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，自v2.9.0版本支持返回地址描述信息
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        //设置获取3s内精度最高的一次定位结果
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息，默认返回地址信息
        mLocationOption.setNeedAddress(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_album:
                startActivity(new Intent(this, LocalAlbumActivity.class));
                break;
            case R.id.iv_commit:
                if (mBinding.ivCommit.isSelected()) {
                    postStatus();
                }
                break;
            case R.id.tv_address:
                if (mBinding.tvAddress.isSelected()) {
                    mBinding.tvAddress.setText("你在哪里？");
                    mBinding.tvAddress.setSelected(false);
                }else {
                    getLocation();
                }
                break;
        }
    }
}
