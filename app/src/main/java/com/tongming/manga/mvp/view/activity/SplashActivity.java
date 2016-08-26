package com.tongming.manga.mvp.view.activity;

import android.content.Intent;

import com.tongming.manga.R;
import com.tongming.manga.mvp.base.BaseActivity;

/**
 * Created by Tongming on 2016/8/26.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}
