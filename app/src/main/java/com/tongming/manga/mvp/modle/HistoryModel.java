package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.db.DBManager;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/15.
 */
public class HistoryModel implements IHistoryModel {

    private onQueryListener queryListener;

    public HistoryModel(onQueryListener queryListener) {
        this.queryListener = queryListener;
    }

    @Override
    public Subscription queryAllHistory() {
        return DBManager.getInstance().queryAllHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HistoryComic>>() {
                    @Override
                    public void call(List<HistoryComic> comics) {
                        queryListener.onQueryAll(comics);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        queryListener.onFail(throwable);
                    }
                });
    }

    @Override
    public Subscription deleteHistoryByName(String name) {
        return DBManager.getInstance().deleteHistoryByName(name)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        queryListener.onDeleteByName(integer);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        queryListener.onFail(throwable);
                    }
                });
    }

    @Override
    public Subscription deleteAllHistory() {
        return DBManager.getInstance().deleteAllHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        queryListener.onDeleteAll(integer);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        queryListener.onFail(throwable);
                    }
                });
    }

    public interface onQueryListener {
        void onQueryAll(List<HistoryComic> comicList);

        void onDeleteByName(int state);

        void onDeleteAll(int state);

        void onFail(Throwable throwable);
    }
}
