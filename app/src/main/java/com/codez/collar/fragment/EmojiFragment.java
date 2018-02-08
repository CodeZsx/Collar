package com.codez.collar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.codez.collar.R;
import com.codez.collar.base.BaseFragment;
import com.codez.collar.databinding.FragmentEmojiBinding;
import com.codez.collar.ui.IndicatorView;
import com.codez.collar.ui.emoji.Emoji;
import com.codez.collar.ui.emoji.EmojiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codez on 2017/12/21.
 * Description:
 */

public class EmojiFragment extends BaseFragment<FragmentEmojiBinding> {

    private IndicatorView mIndicatorView;

    //表情图list
    private ArrayList<Emoji> mEmojiList;
    //表情页list
    private ArrayList<View> mPagerList;
    //默认每页表情图的行列数
    private static final int COLUMNS = 7;
    private static final int ROWS = 3;

    public static EmojiFragment Instance() {
        EmojiFragment instance = new EmojiFragment();
        Bundle bundle = new Bundle();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public int setContent() {
        return R.layout.fragment_emoji;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View root) {

        mEmojiList = EmojiUtil.getEmojiList();
        mPagerList = new ArrayList<>();

        initViewPager();

    }

    private void initViewPager() {
        //初始化圆点指示器
        mBinding.emojiIndicator.init(getPagerCount(mEmojiList), 0, IndicatorView.THEME_LIGHT);
        mBinding.tvDefault.setSelected(true);

        mPagerList.clear();
        for (int i = 0; i < getPagerCount(mEmojiList); i++) {
            mPagerList.add(getPagerView(i, mEmojiList));
        }
        EmojiPagerAdapter mEmojiPagerAdapter = new EmojiPagerAdapter(mPagerList);
        mBinding.viewPager.setAdapter(mEmojiPagerAdapter);
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.emojiIndicator.playTo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //根据表情图数量计算出 COlUMNS-列，ROWS-行 的pager数量
    private int getPagerCount(ArrayList<Emoji> list) {
        int count = list.size();
        //每一页的末尾都有一个删除图标，所以每一页的实际表情数量减一
        int PAGER_SIZE = COLUMNS * ROWS - 1;
        return count % PAGER_SIZE == 0 ? count / PAGER_SIZE : count / PAGER_SIZE + 1;
    }

    //获取表情图页的view
    private View getPagerView(int position, final ArrayList<Emoji> list) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.item_emoji_pager, null);
        GridView gridView = (GridView) layout.findViewById(R.id.gridView);

        //初始化当前position的页面表情图list
        final List<Emoji> pagerList = new ArrayList<>();
        pagerList.addAll(list.subList(position * (COLUMNS * ROWS - 1),
                (COLUMNS * ROWS - 1) * (position + 1) > list.size() ? list.size() : (COLUMNS * ROWS - 1) * (position + 1)));
        //添加末尾的删除图标,中间有空余，则需添加对应数量的null
        if (pagerList.size() < (COLUMNS * ROWS - 1)) {
            for (int i = pagerList.size();i<(COLUMNS*ROWS-1);i++) {
                pagerList.add(null);
            }
        }
        Emoji deleteEmoji = new Emoji();
        deleteEmoji.setResourceId(R.drawable.ic_emoji_delete);
        pagerList.add(deleteEmoji);

        EmojiAdapter mEmojiAdapter = new EmojiAdapter(getContext(), pagerList);
        gridView.setAdapter(mEmojiAdapter);
        gridView.setNumColumns(COLUMNS);

        //表情点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == COLUMNS * ROWS - 1) {
                    if (listener != null) {
                        listener.onEmojiDelete();
                    }
                    return;
                }
                if (listener != null) {
                    listener.onEmojiClick(pagerList.get(position));
                }
                //TODO:添加到最近list中
            }
        });
        return gridView;
    }

    //表情图适配器
    class EmojiAdapter extends BaseAdapter {
        private Context mContext;
        private List<Emoji> mList;

        public EmojiAdapter(Context mContext, List<Emoji> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_emoji, null);
                holder.iv = (ImageView) convertView.findViewById(R.id.iv_emoji);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (mList.get(position) != null) {
                holder.iv.setImageResource(mList.get(position).getResourceId());
//                holder.iv.setImageBitmap(EmojiUtil.decodeSampledBitmapFromResource(getActivity().getResources(), mList.get(position).getResourceId(),
//                        EmojiUtil.dip2px(getActivity(), 32), EmojiUtil.dip2px(getActivity(), 32)));
            }
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
        }
    }

    //emoji点击事件监听器
    private OnEmojiClickListener listener;

    public void addOnEmojiClickListener(OnEmojiClickListener listener) {
        this.listener = listener;
    }

    public interface OnEmojiClickListener{
        void onEmojiDelete();

        void onEmojiClick(Emoji emoji);
    }

    //表情页适配器
    class EmojiPagerAdapter extends PagerAdapter{
        private List<View> views;

        public EmojiPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        //判断是否是由对象生成界面
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view==object);
        }

        //初始化view位置的界面
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }
    }
}
