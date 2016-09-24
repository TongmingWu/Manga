package com.tongming.manga.mvp.db;

import android.content.Context;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.server.DownloadInfo;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/14.
 */
public class DBManager {

    private BriteDatabase briteDatabase;
    private static DBManager instance;
    private final SQLiteHelper helper;

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

    public void openDB() {
        briteDatabase = SqlBrite.create().wrapDatabaseHelper(helper, Schedulers.io());
    }

    public void closeDB() {
        briteDatabase.close();
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

    /**
     * 删除指定收藏
     */
    public int deleteCollectByName(final String name) {
        return briteDatabase.delete(CollectionTable.TABLE_NAME, CollectionTable.COLUMN_NAME + " = ?", name);
    }

    /**
     * 删除所有收藏
     */
    public int deleteAllCollect() {
        return briteDatabase.delete(CollectionTable.TABLE_NAME,
                "DELETE * FROM " + CollectionTable.TABLE_NAME);
    }

    /**
     * 查询下载信息,ComicDetailActivity
     *
     * @param name   漫画名
     * @param status 下载状态
     */
    public Observable<List<DownloadInfo>> queryDownloadInfo(String name, int status) {
        return briteDatabase.createQuery(DownloadTable.TABLE_NAME, "SELECT * FROM "
                        + DownloadTable.TABLE_NAME
                        + " WHERE "
                        + DownloadTable.COMIC_NAME
                        + " = ? AND "
                        + DownloadTable.STATUS
                        + " = "
                        + status
                , name)
                .mapToList(DownloadTable.COMIC_MAPPER);
    }

    /**
     * 查询下载信息,SelectActivity使用
     */
    public Observable<List<DownloadInfo>> queryDownloadInfoByCid(String cid) {
        return briteDatabase.createQuery(DownloadTable.TABLE_NAME, "SELECT * FROM "
                + DownloadTable.TABLE_NAME
                + " WHERE "
                + DownloadTable.COMIC_ID
                + " = ?", cid)
                .mapToList(DownloadTable.COMIC_MAPPER);
    }

    /**
     * 查询下载信息
     *
     * @param info DownloadInfo
     */
    public Observable<List<DownloadInfo>> queryDownloadInfo(DownloadInfo info) {
        return briteDatabase.createQuery(DownloadTable.TABLE_NAME, "SELECT * FROM "
                        + DownloadTable.TABLE_NAME
                        + " WHERE "
                        + DownloadTable.COMIC_NAME
                        + " = ? "
                , info.getComic_name())
                .mapToList(DownloadTable.COMIC_MAPPER);
    }

    /**
     * 查询单个下载信息
     */
    public Observable<List<DownloadInfo>> queryDownloadInfo(String chapterUrl) {
        return briteDatabase.createQuery(DownloadTable.TABLE_NAME, "SELECT * FROM "
                        + DownloadTable.TABLE_NAME
                        + " WHERE "
                        + DownloadTable.CHAPTER_URL
                        + " = ? "
                , chapterUrl)
                .mapToList(DownloadTable.COMIC_MAPPER);
    }

    /**
     * 查询所有下载信息
     */
    public Observable<List<DownloadInfo>> queryAllDownloadInfo() {
        return briteDatabase.createQuery(DownloadTable.TABLE_NAME, "SELECT * FROM " + DownloadTable.TABLE_NAME)
                .mapToList(DownloadTable.COMIC_MAPPER);
    }

    /**
     * 修改下载信息
     */
    public int updateDownloadInfo(DownloadInfo info) {
        return briteDatabase.update(DownloadTable.TABLE_NAME, DownloadTable.toContentValues(info),
                DownloadTable.CHAPTER_URL + " = ?", info.getChapter_url());
    }

    /**
     * 添加下载信息
     */
    public int insertDownloadInfo(DownloadInfo info) {
        return (int) briteDatabase.insert(DownloadTable.TABLE_NAME, DownloadTable.toContentValues(info));
    }

    /**
     * 删除下载信息
     */
    public int deleteDownloadInfo(DownloadInfo info) {
        return briteDatabase.delete(DownloadTable.TABLE_NAME, DownloadTable.CHAPTER_URL + " = ?", info.getChapter_url());
    }


}
