package com.codez.collar.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.R;
import com.codez.collar.adapter.UserAdapter;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.bean.UserBean;
import com.codez.collar.databinding.FragmentBaseListBinding;
import com.codez.collar.event.SearchWordChangedEvent;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.L;
import com.codez.collar.utils.T;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UserListFragment extends BaseFragment<FragmentBaseListBinding> implements View.OnClickListener {

    private static final String TAG = "UserListFragment";

    private int curPage;
    private String queryWord = null;
    private UserAdapter mAdapter;
    @Override
    public int setContent() {
        return R.layout.fragment_base_list;
    }
    @Override
    public void initView(View root) {
        EventBusUtils.register(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mAdapter = new UserAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.swipeRefreshLayout.setEnabled(false);
        loadData();

    }

    private void loadData() {
        if (queryWord != null) {
            HttpUtils.getInstance().getSearchService()
                    .searchUsers(queryWord, 1, 20)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<UserBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            T.s(getContext(), "数据加载失败");
                            L.e(e.toString());
                        }

                        @Override
                        public void onNext(List<UserBean> list) {
                            mAdapter.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    @Override
    public void onDestroy() {
        EventBusUtils.unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupChangedEvent(SearchWordChangedEvent event) {
        if (event == null) {
            return;
        }
        queryWord = event.getSearchWord();
        curPage = 1;
        loadData();
    }

    @Override
    public void onClick(View v) {

    }
}
