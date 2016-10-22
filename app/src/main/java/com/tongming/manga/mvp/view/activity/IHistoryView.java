package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.HistoryComic;

import java.util.List;

/**
 * Created by Tongming on 2016/8/15.
 */
public interface IHistoryView {
    void onQuery(List<HistoryComic> comics);

    void onDeleteByName(int state, String name);

    void onRestoreHistory(int state);

    void onDeleteAll(int state);
}
