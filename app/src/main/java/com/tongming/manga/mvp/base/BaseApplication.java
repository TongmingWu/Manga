package com.tongming.manga.mvp.base;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;


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
        CrashReport.initCrashReport(getApplicationContext(), "900051193", false);
    }

    public static Context getContext() {
        return mContext;
    }
}
