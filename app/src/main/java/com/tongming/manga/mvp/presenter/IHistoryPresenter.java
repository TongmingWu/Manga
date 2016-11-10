package com.tongming.manga.mvp.presenter;

import com.tongming.manga.mvp.bean.HistoryComic;

/**
 * Created by Tongming on 2016/8/15.
 */
interface IHistoryPresenter {
    void queryAllHistory();

    void deleteHistoryByName(String name);

    void restoreHistory(HistoryComic comic);

    void deleteAllHistory();
}
