package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.bean.SearchRecord;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface ISearchModel {
    Subscription doSearch(int type, int page);

    Subscription doSearch(String word, int page);

    Subscription getCategory();

    void recordSearch(SearchRecord record);

    void querySearchRecord();
}
