package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.bean.Search;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/11.
 */
public class SearchModel implements ISearchModel {

    private onSearchListener onSearchListener;

    public SearchModel(SearchModel.onSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    @Override
    public Subscription getComicType(int select, int type, int page) {
        return ApiManager.getInstance().getComicType(select, type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Search>() {
                    @Override
                    public void call(Search search) {
                        onSearchListener.onSuccess(search);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onSearchListener.onFail(throwable);
                    }
                });
    }

    @Override
    public Subscription doSearch(String word, int page) {
        return ApiManager.getInstance().doSearch(word, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Search>() {
                    @Override
                    public void call(Search search) {
                        onSearchListener.onSuccess(search);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onSearchListener.onFail(throwable);
                    }
                });
    }


    public interface onSearchListener {
        void onSuccess(Search search);

        void onFail(Throwable throwable);
    }
}
