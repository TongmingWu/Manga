package com.tongming.manga.mvp.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.tongming.manga.server.DownloadInfo;

import rx.functions.Func1;

/**
 * Author: Tongming
 * Date: 2016/9/7
 */

class DownloadTable {
    static final String TABLE_NAME = "download_comic";

    static final String ID = "_id";
    static final String COMIC_ID = "comic_id";
    static final String COMIC_NAME = "comic_name";
    static final String COMIC_URL = "comic_url";
    static final String CHAPTER_NAME = "chapter_name";
    static final String CHAPTER_URL = "chapter_url";
    static final String COMIC_SOURCE = "comic_source";
    static final String COVER = "cover";
    static final String STATUS = "status";
    static final String POSITION = "position";  //当前下载页
    static final String TOTAL = "total";    //总下载页数
    static final String CREATE_TIME = "create_time";
    static final String NEXT = "next";
    static final String PREPARE = "prepare";
    static final String NEXT_URL = "next_url";
    static final String PRE_URL = "pre_url";

    static final String CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COMIC_ID + " TEXT,"
            + COMIC_NAME + " TEXT,"
            + COMIC_URL + " TEXT,"
            + COMIC_SOURCE + " TEXT,"
            + CHAPTER_NAME + " TEXT,"
            + CHAPTER_URL + " TEXT,"
            + COVER + " TEXT,"
            + STATUS + " INTEGER,"
            + POSITION + " INTEGER,"
            + TOTAL + " INTEGER,"
            + CREATE_TIME + " INTEGER,"
            + NEXT + " INTEGER,"
            + PREPARE + " INTEGER,"
            + NEXT_URL + " TEXT,"
            + PRE_URL + " TEXT"
            + ")";

    static ContentValues toContentValues(DownloadInfo info) {
        ContentValues values = new ContentValues();
        values.put(COMIC_NAME, info.getComic_name());
        values.put(COMIC_ID, info.getComic_id());
        values.put(COMIC_URL, info.getComic_url());
        values.put(CHAPTER_NAME, info.getChapter_name());
        values.put(CHAPTER_URL, info.getChapter_url());
        values.put(COVER, info.getCover());
        values.put(COMIC_SOURCE, info.getComic_source());
        values.put(STATUS, info.getStatus());
        values.put(POSITION, info.getPosition());
        values.put(TOTAL, info.getTotal());
        values.put(CREATE_TIME, info.getCreate_time());
        values.put(NEXT, info.getNext());
        values.put(PREPARE, info.getPrepare());
        values.put(NEXT_URL, info.getNext_url());
        values.put(PRE_URL, info.getPre_url());
        return values;
    }

    static Func1<Cursor, DownloadInfo> COMIC_MAPPER = new Func1<Cursor, DownloadInfo>() {
        @Override
        public DownloadInfo call(Cursor cursor) {
            DownloadInfo info = new DownloadInfo();
            info.setComic_name(cursor.getString(cursor.getColumnIndex(COMIC_NAME)));
            info.setComic_id(cursor.getString(cursor.getColumnIndex(COMIC_ID)));
            info.setComic_url(cursor.getString(cursor.getColumnIndex(COMIC_URL)));
            info.setChapter_name(cursor.getString(cursor.getColumnIndex(CHAPTER_NAME)));
            info.setComic_source(cursor.getString(cursor.getColumnIndex(COMIC_SOURCE)));
            info.setChapter_url(cursor.getString(cursor.getColumnIndex(CHAPTER_URL)));
            info.setCover(cursor.getString(cursor.getColumnIndex(COVER)));
            info.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
            info.setPosition(cursor.getInt(cursor.getColumnIndex(POSITION)));
            info.setTotal(cursor.getInt(cursor.getColumnIndex(TOTAL)));
            info.setCreate_time(cursor.getInt(cursor.getColumnIndex(CREATE_TIME)));
            info.setNext(cursor.getInt(cursor.getColumnIndex(NEXT)));
            info.setPrepare(cursor.getInt(cursor.getColumnIndex(PREPARE)));
            info.setPre_url(cursor.getString(cursor.getColumnIndex(PRE_URL)));
            info.setNext_url(cursor.getString(cursor.getColumnIndex(NEXT_URL)));
            return info;
        }
    };

}
