package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.Category;
import com.tongming.manga.mvp.bean.Search;
import com.tongming.manga.mvp.bean.SearchRecord;

import java.util.List;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface ISearchView {
    void onSuccess(Search search);

    void onQuery(List<SearchRecord> recordList);

    void onGetCategory(Category category);

    void showProgress();

    void hideProgress();

    void onFail(Throwable throwable);
}
