package com.tongming.manga.mvp.modle;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/17.
 */
public interface ICollectModel {
    Subscription queryAllCollect();

    void deleteCollectByName(String name);

    void deleteAllCollect();

    Subscription deleteCollectOnNet(String name);
}
