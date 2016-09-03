package com.tongming.manga.mvp.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tongming on 2016/8/9.
 */
public class BaseApplication extends Application {

    private static Context mContext;
    public static final long DEFAULT_CACHE_CEILING = 1024 * 1024 * 200L;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
