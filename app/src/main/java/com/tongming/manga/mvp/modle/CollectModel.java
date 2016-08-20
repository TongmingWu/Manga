package com.tongming.manga.mvp.modle;

import android.content.Context;

import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.db.DBManager;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/17.
 */
public class CollectModel implements ICollectModel {

    private OnCollectListener collectListener;

    public CollectModel(OnCollectListener collectListener) {
        this.collectListener = collectListener;
    }

    @Override
    public Subscription queryAllCollect(Context context) {
        return new DBManager(context).queryAllCollected()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CollectedComic>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        collectListener.onFail(e);
                    }

                    @Override
                    public void onNext(List<CollectedComic> comics) {
                        collectListener.onQueryAllCompleted(comics);
                    }
                });
    }

    @Override
    public void deleteCollectByName(Context context, String name) {
        int state = new DBManager(context).deleteCollectByName(name);
        collectListener.onDeleteByName(state);

    }

    @Override
    public void deleteAllCollect(Context context) {
        int state = new DBManager(context).deleteAllCollect();
        collectListener.onDeleteAll(state);
    }

    public interface OnCollectListener {
        void onQueryAllCompleted(List<CollectedComic> comics);

        void onDeleteByName(int state);

        void onDeleteAll(int state);

        void onFail(Throwable throwable);
    }
}
