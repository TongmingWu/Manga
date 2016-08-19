package com.tongming.manga.mvp.presenter;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.Search;
import com.tongming.manga.mvp.modle.ISearchModel;
import com.tongming.manga.mvp.modle.SearchModel;
import com.tongming.manga.mvp.view.activity.ISearchView;

/**
 * Created by Tongming on 2016/8/11.
 */
public class SearchPresenterImp extends BasePresenter implements ISearchPresenter, SearchModel.onSearchListener {
    private ISearchModel searchModel;
    private ISearchView searchView;
    private String word;

    public SearchPresenterImp(ISearchView searchView) {
        this.searchView = searchView;
        searchModel = new SearchModel(this);
    }

    @Override
    public void getComicType(int select, int type, int page) {
        if (page == 1) {
            searchView.showProgress();
        }
        addSubscription(searchModel.getComicType(select, type, page));
    }

    @Override
    public void doSearch(String word, int page) {
        if (page == 1) {
            searchView.showProgress();
        }
        addSubscription(searchModel.doSearch(word, page));
    }


    @Override
    public void onSuccess(Search search) {
        searchView.hideProgress();
        searchView.onSuccess(search);
    }

    @Override
    public void onFail(Throwable throwable) {
        searchView.hideProgress();
        searchView.onFail();
        Logger.e(throwable.getMessage());
    }


}
