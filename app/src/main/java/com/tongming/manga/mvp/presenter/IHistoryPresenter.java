package com.tongming.manga.mvp.presenter;

import android.content.Context;

import com.tongming.manga.mvp.bean.HistoryComic;

/**
 * Created by Tongming on 2016/8/15.
 */
interface IHistoryPresenter {
    void queryAllHistory(Context context);

    void deleteHistoryByName(Context context, String name);

    void restoreHistory(Context context, HistoryComic comic);

    void deleteAllHistory(Context context);
}
