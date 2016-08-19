package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.db.DBManager;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
    public Subscription queryAllCollect() {
        return DBManager.getInstance().queryAllCollected()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CollectedComic>>() {
                    @Override
                    public void call(List<CollectedComic> comics) {
                        collectListener.onQueryAllCompleted(comics);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        collectListener.onFail(throwable);
                    }
                });
    }

    @Override
    public Subscription deleteCollectByName(String name) {
        return DBManager.getInstance().deleteCollectByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        collectListener.onDeleteByName(integer);
                    }
                });
    }

    @Override
    public Subscription deleteAllCollect() {
        return DBManager.getInstance().deleteAllCollect()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        collectListener.onDeleteAll(integer);
                    }
                });
    }

    public interface OnCollectListener {
        void onQueryAllCompleted(List<CollectedComic> comics);

        void onDeleteByName(int state);

        void onDeleteAll(int state);

        void onFail(Throwable throwable);
    }
}
