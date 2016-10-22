package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.bean.HistoryComic;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/15.
 */
public interface IHistoryModel {
    Subscription queryAllHistory();

    void deleteHistoryByName(String name);

    void restoreHistory(HistoryComic comic);

    void deleteAllHistory();
}
