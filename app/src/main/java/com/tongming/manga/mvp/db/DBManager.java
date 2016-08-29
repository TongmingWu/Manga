package com.tongming.manga.mvp.db;

import android.content.Context;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.HistoryComic;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/14.
 */
public class DBManager {

    private SQLiteHelper helper;
    private BriteDatabase briteDatabase;
    private static DBManager instance;

    public DBManager(Context context) {
        helper = new SQLiteHelper(context);
        briteDatabase = SqlBrite.create().wrapDatabaseHelper(helper, Schedulers.io());
    }


    /*public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }*/

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

    public long addHistory(final ComicInfo info, final String historyName, final String historyUrl) {
        return briteDatabase.insert(HistoryTable.TABLE_NAME, HistoryTable.toContentValues(info, historyName, historyUrl));
    }

    public int updateHistory(final ComicInfo info, final String historyName, final String historyUrl) {
        return briteDatabase.update(HistoryTable.TABLE_NAME, HistoryTable.toContentValues(historyName, historyUrl),
                HistoryTable.COLUMN_NAME + "=?", info.getComic_name());
    }

    public int deleteHistoryByName(final String name) {
        return briteDatabase.delete(HistoryTable.TABLE_NAME, HistoryTable.COLUMN_NAME + " = ? ", name);
    }

    //清空历史记录
    public int deleteAllHistory() {
        return briteDatabase.delete(HistoryTable.TABLE_NAME, null);
    }

    //收藏漫画
    public long collectComic(final ComicInfo info) {
        return briteDatabase.insert(CollectionTable.TABLE_NAME, CollectionTable.toContentValues(info));
    }

    public Observable<List<CollectedComic>> queryAllCollected() {
        return briteDatabase
                .createQuery(CollectionTable.TABLE_NAME, "SELECT * FROM " + CollectionTable.TABLE_NAME + " ORDER BY last_time DESC")
                .mapToList(CollectionTable.COMIC_MAPPER);
    }

    public Observable<List<CollectedComic>> queryCollectedByName(String name) {
        return briteDatabase.createQuery(CollectionTable.TABLE_NAME, "SELECT * FROM "
                        + CollectionTable.TABLE_NAME
                        + " WHERE "
                        + CollectionTable.COLUMN_NAME
                        + " = ?"
                , name)
                .mapToList(CollectionTable.COMIC_MAPPER);
    }

    //删除指定收藏
    public int deleteCollectByName(final String name) {
        return briteDatabase.delete(CollectionTable.TABLE_NAME, CollectionTable.COLUMN_NAME + " = ?", name);
    }

    //删除所有收藏
    public int deleteAllCollect() {
        return briteDatabase.delete(CollectionTable.TABLE_NAME,
                "DELETE * FROM " + CollectionTable.TABLE_NAME);
    }
}
