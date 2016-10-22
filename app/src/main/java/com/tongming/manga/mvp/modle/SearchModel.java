package com.tongming.manga.mvp.modle;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.bean.Category;
import com.tongming.manga.mvp.bean.Search;
import com.tongming.manga.mvp.bean.SearchRecord;
import com.tongming.manga.mvp.db.DBManager;

import java.util.List;

import rx.Subscriber;
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
    public Subscription getCategory() {
        return ApiManager.getInstance()
                .getCategory()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Category>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onSearchListener.onFail(e);
                    }

                    @Override
                    public void onNext(Category category) {
                        onSearchListener.onGetCateGory(category);
                    }
                });
    }

    @Override
    public Subscription doSearch(int type, int page) {
        return ApiManager.getInstance().getComicType(type, page)
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
                .subscribe(new Subscriber<Search>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onSearchListener.onFail(e);
                    }

                    @Override
                    public void onNext(Search search) {
                        onSearchListener.onSuccess(search);
                    }
                });
    }

    @Override
    public void recordSearch(final SearchRecord record) {
        final DBManager manager = DBManager.getInstance();
        manager.querySearchRecord(record.getComic_url())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SearchRecord>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        manager.closeDB();
                    }

                    @Override
                    public void onNext(List<SearchRecord> recordList) {
                        if (recordList.size() > 0) {
                            Logger.d("更新搜索记录");
                            manager.updateSearchRecord(record);
                        } else {
                            Logger.d("插入搜索记录");
                            manager.insertSearchRecord(record);
                        }
                        this.unsubscribe();
                        manager.closeDB();
                    }
                });
    }

    @Override
    public void querySearchRecord() {
        DBManager.getInstance()
                .querySearchRecord()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<SearchRecord>>() {
                    @Override
                    public void call(List<SearchRecord> recordList) {
                        onSearchListener.onQuery(recordList);
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

        void onGetCateGory(Category category);

        void onQuery(List<SearchRecord> recordList);

        void onFail(Throwable throwable);
    }
}
