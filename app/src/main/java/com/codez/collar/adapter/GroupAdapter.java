package com.codez.collar.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;
import com.codez.collar.bean.Group;
import com.codez.collar.databinding.ItemGroupsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Group> list;
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

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    class BindingViewHolder extends RecyclerView.ViewHolder{

        private ItemGroupsBinding mBinding;

        public BindingViewHolder(ItemGroupsBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bindItem(int pos) {
            if (pos == list.size()) {
                mBinding.tvContent.setBackgroundResource(R.drawable.tv_groups_item_create);
                mBinding.tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO:popup a edittext,and create a new group
                    }
                });
            }else{
                mBinding.setGroup(list.get(pos));
                mBinding.tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
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
