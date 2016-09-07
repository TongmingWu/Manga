package com.tongming.manga.mvp.presenter;

import android.content.Context;

/**
 * Created by Tongming on 2016/8/15.
 */
interface IHistoryPresenter {
    void queryAllHistory(Context context);

    void deleteHistoryByName(Context context,String name);

    void deleteAllHistory(Context context);
}
