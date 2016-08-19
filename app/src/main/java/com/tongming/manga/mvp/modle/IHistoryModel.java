package com.tongming.manga.mvp.modle;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/15.
 */
public interface IHistoryModel {
    Subscription queryAllHistory();

    Subscription deleteHistoryByName(String name);

    Subscription deleteAllHistory();
}
