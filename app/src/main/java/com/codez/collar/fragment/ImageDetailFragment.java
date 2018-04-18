package com.codez.collar.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.codez.collar.R;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.databinding.FragmentImageDetailBinding;
import com.codez.collar.event.ToastEvent;
import com.codez.collar.ui.BottomDialog;
import com.codez.collar.utils.BitmapUtil;
import com.codez.collar.utils.EventBusUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageDetailFragment extends BaseFragment<FragmentImageDetailBinding> implements View.OnClickListener {
    private static final String TAG = "ImageDetailFragment";
    private static final String KEY_URL = "url";

    private String mUrl;
    private int curPage;
    @Override
    public int setContent() {
        return R.layout.fragment_image_detail;
    }

    public static ImageDetailFragment newInstance(String url){
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initView(View root) {
        if (getArguments() != null) {
            mUrl = getArguments().getString(KEY_URL);
        }
        curPage = 1;
        mBinding.photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().finish();
            }
        });
        mBinding.photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final BottomDialog dialog = new BottomDialog(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_picture, null);
//                view.findViewById(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
                view.findViewById(R.id.tv_download).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadPicture();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setView(view).show();
                return false;
            }
        });
        mBinding.setUrl(mUrl);
    }

    private void downloadPicture() {
        Observable.timer(0, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        String[] array = mUrl.split("/");
                        String picPath = BitmapUtil.savePic(mBinding.photoView.getVisibleRectangleBitmap(),
                                array[array.length - 1]);
                        if (picPath != null && !TextUtils.isEmpty(picPath)) {
                            EventBusUtils.sendEvent(ToastEvent.newToastEvent("已保存到：" + picPath));
                        }else{
                            EventBusUtils.sendEvent(ToastEvent.newToastEvent("图片保存失败"));
                        }

                    }
                });
    }

    public void setBgAlpha(float bgAlpha){
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {

    }
}
