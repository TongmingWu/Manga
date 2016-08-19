package com.tongming.manga.mvp.presenter;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface ISearchPresenter {
    void getComicType(int select, int type, int page);

    void doSearch(String word, int page);
}
