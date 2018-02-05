package com.codez.collar.activity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codez.collar.Config;
import com.codez.collar.R;
import com.codez.collar.adapter.ThemeAdapter;
import com.codez.collar.base.BaseActivity;
import com.codez.collar.databinding.ActivityThemeBinding;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.L;

import java.util.ArrayList;
import java.util.List;

import skin.support.SkinCompatManager;

public class ThemeActivity extends BaseActivity<ActivityThemeBinding> {


    //已在colors.xml配置了的theme颜色
    public static final String[] THEME_LIST = {"a", "b", "c", "d",
            "e", "f", "g", "h",
            "i", "j", "k",
            "m", "n", "o", "p"};

    private ThemeAdapter mAdapter;
    private List<ThemeBean> mList;

    @Override
    public int setContent() {
        return R.layout.activity_theme;
    }

    @Override
    public void initView() {
        setToolbarTitle(mBinding.toolbar, "主题样式");

        mAdapter = new ThemeAdapter(this);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int itemPadding = DensityUtil.dp2px(ThemeActivity.this, 8);

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = itemPadding;
                outRect.left = itemPadding;
                outRect.right = itemPadding;
                outRect.top = itemPadding;
            }
        });
        loadData();

        mBinding.btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("theme:" + mAdapter.getCurTheme());
                if (!Config.getCachedTheme(ThemeActivity.this).equals(mAdapter.getCurTheme())) {
                    SkinCompatManager.getInstance().loadSkin(mAdapter.getCurTheme(), SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                    Config.cacheTheme(ThemeActivity.this, mAdapter.getCurTheme());
                    Config.cacheNight(ThemeActivity.this, false);
                }
                ThemeActivity.this.finish();
            }
        });

    }

    private void loadData() {
        mList = new ArrayList<>();
        String curTheme = Config.getCachedTheme(this);
        for (int i = 0; i < THEME_LIST.length; i++) {
            mList.add(new ThemeBean(THEME_LIST[i], curTheme.equals(THEME_LIST[i])));
        }
        mAdapter.addAll(mList);
    }

    public class ThemeBean {
        String theme;
        boolean selected;

        public ThemeBean(String theme, boolean selected) {
            this.theme = theme;
            this.selected = selected;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
