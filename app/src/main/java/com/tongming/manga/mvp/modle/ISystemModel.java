package com.tongming.manga.mvp.modle;

import android.content.Context;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/14.
 */
public interface ISystemModel {
    Subscription clearCache(Context context, boolean clearAll);

    Subscription calculateSize(Context context);

    Subscription getUser(String token);

    void readUser();
}
