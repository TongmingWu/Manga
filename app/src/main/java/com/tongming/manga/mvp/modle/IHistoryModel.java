package com.tongming.manga.mvp.modle;

import android.content.Context;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/15.
 */
public interface IHistoryModel {
    Subscription queryAllHistory(Context context);

    void deleteHistoryByName(Context context, String name);

    void deleteAllHistory(Context context);
}
