package com.tongming.manga.mvp.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;


/**
 * Created by Tongming on 2016/8/9.
 */
public class BaseApplication extends Application {

    private static Context mContext;
    public static final long DEFAULT_CACHE_CEILING = 1024 * 1024 * 200L;
    public static String externalPath;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        CrashReport.initCrashReport(getApplicationContext(), "900051193", false);

        externalPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + getPackageName();
        File file = new File(externalPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getExternalPath() {
        return externalPath;
    }
}
