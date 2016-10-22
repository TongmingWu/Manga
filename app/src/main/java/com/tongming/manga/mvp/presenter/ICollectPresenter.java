package com.tongming.manga.mvp.presenter;

/**
 * Created by Tongming on 2016/8/17.
 */
interface ICollectPresenter {
    void queryAllCollect();

    void deleteCollectByName(String name);

    void deleteAllCollect();

    void deleteCollectOnNet(String name);
}
