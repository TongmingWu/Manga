package com.tongming.manga.mvp.modle;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface ISearchModel {
    Subscription getComicType(int select, int type, int page);

    Subscription doSearch(String word, int page);
}
