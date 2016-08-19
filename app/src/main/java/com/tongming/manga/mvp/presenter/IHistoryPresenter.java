package com.tongming.manga.mvp.presenter;

/**
 * Created by Tongming on 2016/8/15.
 */
public interface IHistoryPresenter {
    void queryAllHistory();

    void deleteHistoryByName(String name);

    void deleteAllHistory();
}
