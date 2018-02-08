package com.codez.collar.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codez.collar.R;
import com.codez.collar.bean.DirectMsgBean;
import com.codez.collar.databinding.ItemMsgLeftBinding;
import com.codez.collar.databinding.ItemMsgRightBinding;
import com.codez.collar.event.ToastEvent;
import com.codez.collar.ui.emojitextview.StatusContentTextUtil;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.TimeUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class DirectMsgConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<DirectMsgBean> list;
    //记录私信对象用户的uid
    private String mUid;
    private static final int VIEW_TYPE_LEFT = 0;
    private static final int VIEW_TYPE_RIGHT = 1;

    private String lastTime;

    public DirectMsgConversationAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        this.mUid = uid;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == VIEW_TYPE_LEFT) {
            ItemMsgLeftBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_msg_left, parent, false);
            return new LeftViewHolder(mBinding);
        }else{
            ItemMsgRightBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_msg_right, parent, false);
            return new RightViewHolder(mBinding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!list.get(position).getSender_id().equals(mUid)) {
            return VIEW_TYPE_LEFT;
        }
        return VIEW_TYPE_RIGHT;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LeftViewHolder) {
            ((LeftViewHolder) holder).bindItem(list.get(position), position);
        }else{
            ((RightViewHolder) holder).bindItem(list.get(position), position);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class LeftViewHolder extends RecyclerView.ViewHolder {

        private ItemMsgLeftBinding mBinding;

        public LeftViewHolder(ItemMsgLeftBinding binding) {
            super(binding.llRoot);
            this.mBinding = binding;
        }
        private void bindItem(final DirectMsgBean bean, int position){
            mBinding.setMsg(bean);
            if (position + 1 < list.size()) {
                try {
                    mBinding.tvDate.setVisibility(TimeUtil.isSamePeriod(list.get(position+1).created_at, bean.created_at) ? View.GONE : View.VISIBLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            mBinding.tvText.setText(StatusContentTextUtil.getWeiBoContent(bean.getText(),
                    mContext, mBinding.tvText));
            mBinding.tvText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager cm =(ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText("text", bean.getText()));
                    EventBusUtils.sendEvent(ToastEvent.newToastEvent("已复制到剪切板"));
                    return false;
                }
            });
            mBinding.executePendingBindings();
        }
    }
    class RightViewHolder extends RecyclerView.ViewHolder {

        private ItemMsgRightBinding mBinding;

        public RightViewHolder(ItemMsgRightBinding binding) {
            super(binding.llRoot);
            this.mBinding = binding;
        }
        private void bindItem(final DirectMsgBean bean, int position){
            mBinding.setMsg(bean);
            if (position + 1 < list.size()) {
                try {
                    mBinding.tvDate.setVisibility(TimeUtil.isSamePeriod(list.get(position+1).created_at, bean.created_at) ? View.GONE : View.VISIBLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            lastTime = bean.created_at;

            mBinding.tvText.setText(StatusContentTextUtil.getWeiBoContent(bean.getText(),
                    mContext, mBinding.tvText));
            mBinding.tvText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager cm =(ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText("text", bean.getText()));
                    EventBusUtils.sendEvent(ToastEvent.newToastEvent("已复制到剪切板"));
                    return false;
                }
            });
            mBinding.executePendingBindings();
        }
    }


    public void setList(List<DirectMsgBean> list) {
        this.list.clear();
        this.list = list;
    }

    public void add(DirectMsgBean bean) {
        this.list.add(bean);
    }
    public void addToFirst(DirectMsgBean bean){
        this.list.add(0,bean);
    }
    public void addAll(List<DirectMsgBean> list){
        this.list.addAll(list);
    }
}
