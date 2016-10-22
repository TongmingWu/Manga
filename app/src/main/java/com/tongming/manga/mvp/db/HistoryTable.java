package com.tongming.manga.mvp.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.HistoryComic;

import rx.functions.Func1;

/**
 * Created by Tongming on 2016/8/15.
 */
public class HistoryTable {
    static final String TABLE_NAME = "history_comic";

    // 表字段
    static final String ID = "_id";
    static final String COMIC_ID = "comic_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_AUTHOR = "author";
    static final String COLUMN_SOURCE = "source";
    static final String COLUMN_AREA = "area";
    static final String COLUMN_TYPE = "type";
    static final String COLUMN_URL = "url";
    static final String COLUMN_CHAPTER_NUM = "chapter_num";
    static final String COLUMN_STATUS = "status";
    static final String COLUMN_HISTORY_NAME = "history_name";
    static final String COLUMN_HISTORY_URL = "history_url";
    static final String COLUMN_COVER = "cover";
    static final String COLUMN_TIME = "last_time";


    static final String CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COMIC_ID + " TEXT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_AUTHOR + " TEXT,"
            + COLUMN_SOURCE + " TEXT,"
            + COLUMN_AREA + " TEXT,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_URL + " VARCHAR(20),"
            + COLUMN_CHAPTER_NUM + " INTEGER,"
            + COLUMN_STATUS + " INTEGER,"
            + COLUMN_HISTORY_NAME + " VARCHAR(20),"
            + COLUMN_HISTORY_URL + " VARCHAR(20),"
            + COLUMN_COVER + " VARCHAR(60),"
            + COLUMN_TIME + " INTEGER"
            + ")";

    // 对象转字段,放入表中
    static ContentValues toContentValues(ComicInfo info, String historyName, String historyUrl) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, info.getComic_name());
        values.put(COMIC_ID, info.getComic_id());
        values.put(COLUMN_AUTHOR, info.getComic_author());
        values.put(COLUMN_AREA, info.getComic_area());
        values.put(COLUMN_TYPE, info.getComic_type());
        values.put(COLUMN_SOURCE, info.getComic_source());
        values.put(COLUMN_URL, info.getComic_url());
        values.put(COLUMN_CHAPTER_NUM, info.getChapter_list().size());
        values.put(COLUMN_STATUS, info.getStatus().contains("连载") ? 0 : 1);
        values.put(COLUMN_HISTORY_NAME, historyName);
        values.put(COLUMN_HISTORY_URL, historyUrl);
        values.put(COLUMN_COVER, info.getCover());
        values.put(COLUMN_TIME, System.currentTimeMillis());
        return values;
    }

    static ContentValues toContentValues(HistoryComic comic) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, comic.getName());
        values.put(COMIC_ID, comic.getComic_id());
        values.put(COLUMN_AUTHOR, comic.getAuthor());
        values.put(COLUMN_AREA, comic.getArea());
        values.put(COLUMN_TYPE, comic.getType());
        values.put(COLUMN_SOURCE, comic.getComic_source());
        values.put(COLUMN_URL, comic.getUrl());
        values.put(COLUMN_CHAPTER_NUM, comic.getChapterNum());
        values.put(COLUMN_STATUS, comic.getStatus());
        values.put(COLUMN_HISTORY_NAME, comic.getHistoryName());
        values.put(COLUMN_HISTORY_URL, comic.getHistoryUrl());
        values.put(COLUMN_COVER, comic.getCover());
        values.put(COLUMN_TIME, System.currentTimeMillis());
        return values;
    }

    static ContentValues toContentValues(String historyName, String historyUrl) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_HISTORY_NAME, historyName);
        values.put(COLUMN_HISTORY_URL, historyUrl);
        values.put(COLUMN_TIME, System.currentTimeMillis());
        return values;
    }

    // 响应式的查询,根据表中的row生成一个对象
    static Func1<Cursor, HistoryComic> COMIC_MAPPER = new Func1<Cursor, HistoryComic>() {
        @Override
        public HistoryComic call(Cursor cursor) {
            HistoryComic comic = new HistoryComic();
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            comic.setName(name);
            String comicId = cursor.getString(cursor.getColumnIndex(COMIC_ID));
            comic.setComic_id(comicId);
            comic.setComic_source(cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE)));
            String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
            comic.setAuthor(author);
            String area = cursor.getString(cursor.getColumnIndex(COLUMN_AREA));
            comic.setArea(area);
            String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
            comic.setType(type);
            String url = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
            comic.setUrl(url);
            int num = cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_NUM));
            comic.setChapterNum(num);
            int status = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS));
            comic.setStatus(status);
            String historyName = cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_NAME));
            comic.setHistoryName(historyName);
            String historyUrl = cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_URL));
            comic.setHistoryUrl(historyUrl);
            String cover = cursor.getString(cursor.getColumnIndex(COLUMN_COVER));
            comic.setCover(cover);
            long lastTime = cursor.getLong(cursor.getColumnIndex(COLUMN_TIME));
            comic.setLastTime(lastTime);
            return comic;
        }
    };
}
