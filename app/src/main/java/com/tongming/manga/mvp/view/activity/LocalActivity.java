package com.tongming.manga.mvp.view.activity;

import android.widget.ImageView;

import com.tongming.manga.R;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.base.SwipeBackActivity;
import com.tongming.manga.util.HeaderGlide;

import butterknife.BindView;

/**
 * Author: Tongming
 * Date: 2016/9/20
 */

public class LocalActivity extends SwipeBackActivity {
    @BindView(R.id.iv_test)
    ImageView ivTest;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_local;
    }

    @Override
    protected void initView() {
        String url = "http://images.dmzj.com/img/chapterpic/20457/55239/14743796761525.jpg";

        HeaderGlide.loadImage(this, url, ivTest, ApiManager.SOURCE_DMZJ);
    }

}
