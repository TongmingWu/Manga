package com.tongming.manga.mvp.db;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.HistoryComic;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/14.
 */
public class DBManager {

    private SQLiteHelper helper;
    private BriteDatabase briteDatabase;
    private static DBManager instance;

    private DBManager() {
        helper = new SQLiteHelper(BaseApplication.getContext());
        briteDatabase = SqlBrite.create().wrapDatabaseHelper(helper, Schedulers.io());
    }


    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public Observable<List<HistoryComic>> queryAllHistory() {
        //查询所有历史记录
        return briteDatabase
                .createQuery(HistoryTable.TABLE_NAME, "SELECT * FROM " + HistoryTable.TABLE_NAME + " ORDER BY last_time DESC")
                .mapToList(HistoryTable.COMIC_MAPPER);
    }

    //根据漫画名查询历史记录
    public Observable<List<HistoryComic>> queryHistoryByName(String name) {
        return briteDatabase.createQuery(HistoryTable.TABLE_NAME, "SELECT * FROM "
                        + HistoryTable.TABLE_NAME
                        + " WHERE "
                        + HistoryTable.COLUMN_NAME
                        + " = ?"
                , name)
                .mapToList(HistoryTable.COMIC_MAPPER);
    }

    public Observable<Long> addHistory(final ComicInfo info, final String historyName, final String historyUrl) {
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                briteDatabase.insert(HistoryTable.TABLE_NAME, HistoryTable.toContentValues(info, historyName, historyUrl));
            }
        });
    }

    public Observable<Integer> updateHistory(final ComicInfo info, final String historyName, final String historyUrl) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                briteDatabase.update(HistoryTable.TABLE_NAME, HistoryTable.toContentValues(historyName, historyUrl),
                        HistoryTable.COLUMN_NAME + "=?", info.getComic_name());
            }
        });
    }

    public Observable<Integer> deleteHistoryByName(final String name) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                briteDatabase.delete(HistoryTable.TABLE_NAME, HistoryTable.COLUMN_NAME + " = ? ", name);
            }
        });
    }

    //清空历史记录
    public Observable<Integer> deleteAllHistory() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                briteDatabase.delete(HistoryTable.TABLE_NAME, null);
            }
        });
    }

    //收藏漫画
    public Observable<Long> collectComic(final ComicInfo info) {
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                briteDatabase.insert(CollectedTable.TABLE_NAME, CollectedTable.toContentValues(info));
            }
        });
    }

    public Observable<List<CollectedComic>> queryAllCollected() {
        return briteDatabase
                .createQuery(CollectedTable.TABLE_NAME, "SELECT * FROM " + CollectedTable.TABLE_NAME + " ORDER BY last_time DESC")
                .mapToList(CollectedTable.COMIC_MAPPER);
    }

    public Observable<List<CollectedComic>> queryCollectedByName(String name) {
        return briteDatabase.createQuery(CollectedTable.TABLE_NAME, "SELECT * FROM "
                        + CollectedTable.TABLE_NAME
                        + " WHERE "
                        + CollectedTable.COLUMN_NAME
                        + " = ?"
                , name)
                .mapToList(CollectedTable.COMIC_MAPPER);
    }

    //删除指定收藏
    public Observable<Integer> deleteCollectByName(final String name) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                briteDatabase.delete(CollectedTable.TABLE_NAME, CollectedTable.COLUMN_NAME + " = ?", name);
            }
        });
    }

    //删除所有收藏
    public Observable<Integer> deleteAllCollect() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                briteDatabase.delete(CollectedTable.TABLE_NAME,
                        "DELETE * FROM " + CollectedTable.TABLE_NAME);
            }
        });
    }
}
