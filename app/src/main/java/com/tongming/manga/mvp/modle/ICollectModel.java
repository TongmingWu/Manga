package com.tongming.manga.mvp.modle;

import android.content.Context;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/17.
 */
public interface ICollectModel {
    Subscription queryAllCollect(Context context);

    void deleteCollectByName(Context context,String name);

    void deleteAllCollect(Context context);
}
