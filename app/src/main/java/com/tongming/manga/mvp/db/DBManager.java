package com.tongming.manga.mvp.db;

import android.content.Context;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.bean.SearchRecord;
import com.tongming.manga.server.DownloadInfo;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/14.
 */
public class DBManager {

    private static BriteDatabase briteDatabase;
    private static DBManager instance;
    private static SQLiteHelper helper;
    private static final Context mContext = BaseApplication.getContext();

    private DBManager() {
    }

    public static DBManager getInstance() {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager();
                }
            }
        }
        openDB();
        return instance;
    }

    private synchronized static void openDB() {
        if (helper == null) {
            helper = new SQLiteHelper(mContext);
            briteDatabase = SqlBrite.create().wrapDatabaseHelper(helper, Schedulers.io());
        }
    }

    public synchronized void closeDB() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }

    /**
     * 查询所有历史记录
     */
    public Observable<List<HistoryComic>> queryAllHistory() {
        return briteDatabase
                .createQuery(HistoryTable.TABLE_NAME, "SELECT * FROM " + HistoryTable.TABLE_NAME + " ORDER BY last_time DESC")
                .mapToList(HistoryTable.COMIC_MAPPER);
    }

    /**
     * 根据漫画名查询历史记录
     *
     * @param name 漫画名
     */
    public Observable<List<HistoryComic>> queryHistoryByName(String name) {
        return briteDatabase.createQuery(HistoryTable.TABLE_NAME, "SELECT * FROM "
                        + HistoryTable.TABLE_NAME
                        + " WHERE "
                        + HistoryTable.COLUMN_NAME
                        + " = ?"
                , name)
                .mapToList(HistoryTable.COMIC_MAPPER);
    }

    /**
     * 添加历史记录
     *
     * @param info        漫画信息
     * @param historyName 漫画名
     * @param historyUrl  URL
     */
    public long insertHistory(ComicInfo info, String historyName, String historyUrl) {
        return briteDatabase.insert(HistoryTable.TABLE_NAME, HistoryTable.toContentValues(info, historyName, historyUrl));
    }

    /**
     * 添加历史记录
     *
     * @param comic 历史记录信息
     */
    public long insertHistory(HistoryComic comic) {
        return briteDatabase.insert(HistoryTable.TABLE_NAME, HistoryTable.toContentValues(comic));
    }

    /**
     * 更新历史记录
     *
     * @param info        漫画信息
     * @param historyName 漫画名
     * @param historyUrl  URL
     */
    public int updateHistory(ComicInfo info, String historyName, String historyUrl) {
        return briteDatabase.update(HistoryTable.TABLE_NAME, HistoryTable.toContentValues(historyName, historyUrl),
                HistoryTable.COLUMN_NAME + "=?", info.getComic_name());
    }


    /**
     * 根据漫画名删除历史记录
     *
     * @param name 漫画名
     */
    public int deleteHistoryByName(String name) {
        return briteDatabase.delete(HistoryTable.TABLE_NAME, HistoryTable.COLUMN_NAME + " = ? ", name);
    }

    /**
     * 删除所有历史记录
     */
    public int deleteAllHistory() {
        return briteDatabase.delete(HistoryTable.TABLE_NAME, null);
    }

    /**
     * 收藏漫画
     *
     * @param info 漫画信息
     */
    public long collectComic(ComicInfo info) {
        return briteDatabase.insert(CollectionTable.TABLE_NAME, CollectionTable.toContentValues(info));
    }

    /**
     * 查询所有已收藏的漫画
     */
    public Observable<List<CollectedComic>> queryAllCollected() {
        return briteDatabase
                .createQuery(CollectionTable.TABLE_NAME, "SELECT * FROM " + CollectionTable.TABLE_NAME + " ORDER BY last_time DESC")
                .mapToList(CollectionTable.COMIC_MAPPER);
    }

    /**
     * 根据漫画名字查询收藏
     *
     * @param name 漫画名
     */
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
     *
     * @param chapterUrl URL
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
     *
     * @param info 下载信息
     */
    public int updateDownloadInfo(DownloadInfo info) {
        return briteDatabase.update(DownloadTable.TABLE_NAME, DownloadTable.toContentValues(info),
                DownloadTable.CHAPTER_URL + " = ?", info.getChapter_url());
    }

    /**
     * 添加下载信息
     *
     * @param info 下载信息
     */
    public int insertDownloadInfo(DownloadInfo info) {
        return (int) briteDatabase.insert(DownloadTable.TABLE_NAME, DownloadTable.toContentValues(info));
    }

    /**
     * 删除下载信息
     *
     * @param info 下载信息
     */
    public int deleteDownloadInfo(DownloadInfo info) {
        return briteDatabase.delete(DownloadTable.TABLE_NAME, DownloadTable.CHAPTER_URL + " = ?", info.getChapter_url());
    }

    public int deleteDownloadInfo(String cid) {
        return briteDatabase.delete(DownloadTable.TABLE_NAME, DownloadTable.COMIC_ID + " = ?", cid);
    }

    public int deleteDownloadInfoByUrl(String url) {
        return briteDatabase.delete(DownloadTable.TABLE_NAME, DownloadTable.CHAPTER_URL + " = ?", url);
    }

    /**
     * 添加搜索记录
     *
     * @param record 搜索记录
     */
    public int insertSearchRecord(SearchRecord record) {
        return (int) briteDatabase.insert(SearchTable.TABLE_NAME,
                SearchTable.toContentValues(record));
    }

    /**
     * 更新搜索记录
     *
     * @param record 搜索记录
     */
    public int updateSearchRecord(SearchRecord record) {
        return briteDatabase.update(SearchTable.TABLE_NAME,
                SearchTable.toContentValues(record),
                SearchTable.COLUMN_URL + " = ?", record.getComic_url());
    }

    /**
     * 查询搜索记录
     */
    public Observable<List<SearchRecord>> querySearchRecord() {
        return briteDatabase.createQuery(SearchTable.TABLE_NAME, "SELECT * FROM " + SearchTable.TABLE_NAME)
                .mapToList(SearchTable.COMIC_MAPPER);
    }

    /**
     * 查询搜索记录
     *
     * @param url 搜索历史
     */
    public Observable<List<SearchRecord>> querySearchRecord(String url) {
        return briteDatabase.createQuery(SearchTable.TABLE_NAME, "SELECT * FROM "
                + SearchTable.TABLE_NAME
                + " WHERE "
                + SearchTable.COLUMN_URL
                + " = ?", url)
                .mapToList(SearchTable.COMIC_MAPPER);
    }


}
