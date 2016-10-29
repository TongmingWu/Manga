package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.base.BaseModel;
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
public class HistoryModel extends BaseModel implements IHistoryModel {

    private onQueryListener queryListener;

    public HistoryModel(onQueryListener queryListener) {
        this.queryListener = queryListener;
        manager = DBManager.getInstance();
    }

    @Override
    public Subscription queryAllHistory() {
        return manager.queryAllHistory()
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
                        Collections.sort(comics);
                        queryListener.onQueryAll(comics);
                        this.unsubscribe();
                    }
                });
    }

    @Override
    public void deleteHistoryByName(String name) {
        int state = manager.deleteHistoryByName(name);
        queryListener.onDeleteByName(state, name);
    }

    @Override
    public void restoreHistory(HistoryComic comic) {
        int state = (int) manager.insertHistory(comic);
        queryListener.onRestoreHistory(state);
    }

    @Override
    public void deleteAllHistory() {
        int state = manager.deleteAllHistory();
        queryListener.onDeleteAll(state);
    }

    public interface onQueryListener {
        void onQueryAll(List<HistoryComic> comicList);

        void onDeleteByName(int state, String name);

        void onRestoreHistory(int state);

        void onDeleteAll(int state);

        void onFail(Throwable throwable);
    }
}
