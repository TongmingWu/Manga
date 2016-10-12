package com.tongming.manga.mvp.presenter;

/**
 * Created by Tongming on 2016/8/11.
 */
interface ISearchPresenter {
    void doSearch(int select, int type, int page);

    void doSearch(String word, int page);

    void doSearch(String word);

    void recordSearch(String name, String url);

    void querySearchRecord();
}
