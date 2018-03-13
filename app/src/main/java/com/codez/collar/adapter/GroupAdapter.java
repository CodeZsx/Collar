package com.codez.collar.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;
import com.codez.collar.bean.Group;
import com.codez.collar.databinding.ItemGroupsBinding;
import com.codez.collar.event.GroupChangedEvent;
import com.codez.collar.fragment.HomeFragment;
import com.codez.collar.utils.EventBusUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "GroupAdapter";
    private Context mContext;
    private List<Group> list;
    private String selectId = null;
    private static final int VIEW_TYPE_GROUP = 0;
    private static final int VIEW_TYPE_CREATE = 1;

    public GroupAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
            ItemGroupsBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_groups, parent, false);
            return new BindingViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BindingViewHolder) holder).bindItem(position);
    }

    //长度加2的原因是：队头增加一个"全部"、队尾增加一个"新建分组"
    @Override
    public int getItemCount() {
        return list.size()+2;
    }

    class BindingViewHolder extends RecyclerView.ViewHolder{

        private ItemGroupsBinding mBinding;

        public BindingViewHolder(ItemGroupsBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bindItem(final int pos) {
            if (pos == list.size()+1) {
                Group group = new Group();
                group.setName("新建分组");
                mBinding.setGroup(group);
                mBinding.tvContent.setBackgroundResource(R.drawable.tv_groups_item_create);
                mBinding.tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO:popup a edittext,and create a new group
                    }
                });
            } else if (pos == 0) {
                Group group = new Group();
                group.setName(HomeFragment.STATUS_GROUP_ALL);
                mBinding.setGroup(group);
                mBinding.tvContent.setSelected(selectId == null);
                mBinding.tvContent.setBackgroundResource(R.drawable.tv_groups_item);
                mBinding.tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBusUtils.sendEvent(new GroupChangedEvent(GroupChangedEvent.TYPE_ALL, HomeFragment.STATUS_GROUP_ALL, null));
                    }
                });
            } else {
                final Group group = list.get(pos-1);
                mBinding.setGroup(group);
                mBinding.tvContent.setSelected(group.getId().equals(selectId));
                mBinding.tvContent.setBackgroundResource(R.drawable.tv_groups_item);
                mBinding.tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectId = group.getId();
                        GroupAdapter.this.notifyDataSetChanged();
                        EventBusUtils.sendEvent(new GroupChangedEvent(GroupChangedEvent.TYPE_GROUP, group.getName(), group.getId()));
                    }
                });
            }

        }
    }

    public void setList(List<Group> list) {
        this.list.clear();
        this.list = list;
    }

    public List<Group> getList() {
        return list;
    }
    public void add(Group bean) {
        this.list.add(bean);
    }
    public void addAll(List<Group> list){
        this.list.addAll(list);
    }
    public void clear() {
        list.clear();
    }
}
