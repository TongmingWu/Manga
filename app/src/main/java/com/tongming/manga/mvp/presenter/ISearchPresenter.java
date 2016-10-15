package com.tongming.manga.mvp.presenter;

import com.tongming.manga.mvp.bean.SearchRecord;

/**
 * Created by Tongming on 2016/8/11.
 */
interface ISearchPresenter {
    void doSearch(int type, int page);

    void doSearch(String word, int page);

    void doSearch(String word);

    void getCategory();

    void recordSearch(SearchRecord record);

    void querySearchRecord();
}
