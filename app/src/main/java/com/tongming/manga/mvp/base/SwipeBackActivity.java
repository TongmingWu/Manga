package com.tongming.manga.mvp.base;

import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.oubowu.slideback.SlideBackHelper;
import com.oubowu.slideback.SlideConfig;
import com.oubowu.slideback.callbak.OnSlideListener;

/**
 * Author: Tongming
 * Date: 2016/10/28
 */

public abstract class SwipeBackActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SlideBackHelper.attach(
                this,
                BaseApplication.getActivityHelper(),
                new SlideConfig.Builder()
                        .edgeOnly(false)
                        .lock(false)
                        .slideOutPercent(0.5f)
                        .create(),
                new OnSlideListener() {
                    @Override
                    public void onSlide(@FloatRange(from = 0.0,
                            to = 1.0) float percent) {
                    }

                    @Override
                    public void onOpen() {
                    }

                    @Override
                    public void onClose() {
                    }
                }
        );
    }

}
