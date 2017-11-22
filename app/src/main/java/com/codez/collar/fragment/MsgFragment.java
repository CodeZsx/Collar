package com.codez.collar.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;


public class MsgFragment extends Fragment implements View.OnClickListener {

    private String mTabs[] = new String[]{"@我","评论","赞","私信"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_msg, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
    }

    @Override
    public void onClick(View v) {


    }
}
