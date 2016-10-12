package com.tongming.manga.mvp.presenter;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.Search;
import com.tongming.manga.mvp.bean.SearchRecord;
import com.tongming.manga.mvp.modle.ISearchModel;
import com.tongming.manga.mvp.modle.SearchModel;
import com.tongming.manga.mvp.view.activity.ISearchView;

import java.util.List;

/**
 * Created by Tongming on 2016/8/11.
 */
public class SearchPresenterImp extends BasePresenter implements ISearchPresenter, SearchModel.onSearchListener {
    private ISearchModel searchModel;
    private ISearchView searchView;

    public SearchPresenterImp(ISearchView searchView) {
        this.searchView = searchView;
        searchModel = new SearchModel(this);
    }

    @Override
    public void doSearch(int select, int type, int page) {
        if (page == 0) {
            searchView.showProgress();
        }
        addSubscription(searchModel.doSearch(select, type, page));
    }

    @Override
    public void doSearch(String word, int page) {
        if (page == 0) {
            searchView.showProgress();
        }
        addSubscription(searchModel.doSearch(word, page));
    }

    @Override
    public void doSearch(String word) {
        addSubscription(searchModel.doSearch(word, 0));
    }

    @Override
    public void recordSearch(String name, String url) {
        searchModel.recordSearch(name, url);
    }

    @Override
    public void querySearchRecord() {
        searchModel.querySearchRecord();
    }

    @Override
    public void onSuccess(Search search) {
//        searchView.hideProgress();
        searchView.onSuccess(search);
    }

    @Override
    public void onQuery(List<SearchRecord> recordList) {
        searchView.onQuery(recordList);
    }

    @Override
    public void onFail(Throwable throwable) {
        searchView.hideProgress();
        searchView.onFail();
        Logger.e(throwable.getMessage());
    }


}
