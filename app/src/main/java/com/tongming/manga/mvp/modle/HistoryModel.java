package com.tongming.manga.mvp.modle;

import android.content.Context;

import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.db.DBManager;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
    public Subscription queryAllHistory(Context context) {
        return new DBManager(context).queryAllHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<HistoryComic>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        queryListener.onFail(e);
                    }

                    @Override
                    public void onNext(List<HistoryComic> comics) {
                        queryListener.onQueryAll(comics);
                    }
                });
    }

    @Override
    public void deleteHistoryByName(Context context, String name) {
        int state = new DBManager(context).deleteHistoryByName(name);
        queryListener.onDeleteByName(state);
    }

    @Override
    public void deleteAllHistory(Context context) {
        int state = new DBManager(context).deleteAllHistory();
        queryListener.onDeleteAll(state);
    }

    public interface onQueryListener {
        void onQueryAll(List<HistoryComic> comicList);

        void onDeleteByName(int state);

        void onDeleteAll(int state);

        void onFail(Throwable throwable);
    }
}
