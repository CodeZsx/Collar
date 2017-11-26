package com.codez.collar.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.codez.collar.R;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.databinding.FragmentImageDetailBinding;
import com.codez.collar.utils.T;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageDetailFragment extends BaseFragment<FragmentImageDetailBinding> implements View.OnClickListener {

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
                //TODO:保存图片弹出框
                setBgAlpha(0.5f);//背景虚化
                final Dialog dialog_picture = new Dialog(getActivity(), R.style.DialogStatement);
                dialog_picture.setContentView(LayoutInflater.from(getActivity())
                        .inflate(R.layout.dialog_picture, null));
                dialog_picture.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        setBgAlpha(1.0f);//背景虚化恢复
                    }
                });
                WindowManager.LayoutParams lp = dialog_picture.getWindow().getAttributes(); // 获取对话框当前的参数值
                lp.width = (int) (getActivity().getWindowManager().getDefaultDisplay().getWidth() * 1.0); // 宽度设置为屏幕的
                lp.gravity = Gravity.BOTTOM;
                dialog_picture.getWindow().setAttributes(lp);
                dialog_picture.getWindow().setWindowAnimations(R.style.SelectPicStyle);//设置进出动画
                dialog_picture.findViewById(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.s(getActivity(), "share TODO");
                        dialog_picture.dismiss();
                    }
                });
                dialog_picture.findViewById(R.id.tv_download).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        downloadPicture();
                        //TODO:downloadPicture();
                        T.s(getContext(), "TODO:save picture");
                        dialog_picture.dismiss();
                    }
                });
                dialog_picture.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_picture.dismiss();
                    }
                });
                dialog_picture.show();
                return false;
            }
        });
        mBinding.setUrl(mUrl);

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
