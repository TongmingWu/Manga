package com.tongming.manga.mvp.modle;

import android.content.Context;

import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.db.DBManager;

import java.util.Collections;
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
    private Context mContext;

    public HistoryModel(Context context, onQueryListener queryListener) {
        mContext = context;
        this.queryListener = queryListener;
    }

    @Override
    public Subscription queryAllHistory(Context context) {
        final DBManager manager = new DBManager(mContext);
        manager.openDB();
        return manager.queryAllHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<HistoryComic>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                        manager.closeDB();
                    }

                    @Override
                    public void onError(Throwable e) {
                        queryListener.onFail(e);
                        manager.closeDB();
                    }

                    @Override
                    public void onNext(List<HistoryComic> comics) {
                        Collections.sort(comics);
                        queryListener.onQueryAll(comics);
                    }
                });
    }

    @Override
    public void deleteHistoryByName(Context context, String name) {
        DBManager manager = new DBManager(mContext);
        manager.openDB();
        int state = manager.deleteHistoryByName(name);
        manager.closeDB();
        queryListener.onDeleteByName(state);
    }

    @Override
    public void deleteAllHistory(Context context) {
        DBManager manager = new DBManager(mContext);
        manager.openDB();
        int state = manager.deleteAllHistory();
        manager.closeDB();
        queryListener.onDeleteAll(state);
    }

    public interface onQueryListener {
        void onQueryAll(List<HistoryComic> comicList);

        void onDeleteByName(int state);

        void onDeleteAll(int state);

        void onFail(Throwable throwable);
    }
}
