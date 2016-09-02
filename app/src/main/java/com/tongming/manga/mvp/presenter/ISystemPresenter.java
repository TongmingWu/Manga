package com.tongming.manga.mvp.presenter;

import android.content.Context;

/**
 * Created by Tongming on 2016/8/14.
 */
public interface ISystemPresenter {
    void clearCache(Context context, boolean clearAll);

    void calculateCacheSize(Context context);

    void getUser(String token);

    void readUser();
}
