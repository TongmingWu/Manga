package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.Search;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface ISearchView {
    void onSuccess(Search search);

    void showProgress();

    void hideProgress();

    void onFail();
}
