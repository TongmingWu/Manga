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
    public static final String TABLE_NAME = "history_comic";

    // 表字段
    public static final String ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_AREA = "area";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_CHAPTER_NUM = "chapter_num";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_HISTORY_NAME = "history_name";
    public static final String COLUMN_HISTORY_URL = "history_url";
    public static final String COLUMN_COVER = "cover";
    public static final String COLUMN_TIME = "last_time";


    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_AUTHOR + " TEXT,"
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
    public static ContentValues toContentValues(ComicInfo info, String historyName, String historyUrl) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, info.getComic_name());
        values.put(COLUMN_AUTHOR, info.getComic_author());
        values.put(COLUMN_AREA, info.getComic_area());
        values.put(COLUMN_TYPE, info.getComic_type());
        values.put(COLUMN_URL, info.getComic_url());
        values.put(COLUMN_CHAPTER_NUM, info.getChapter_list().size());
        values.put(COLUMN_STATUS, info.getStatus().contains("连载") ? 0 : 1);
        values.put(COLUMN_HISTORY_NAME, historyName);
        values.put(COLUMN_HISTORY_URL, historyUrl);
        values.put(COLUMN_COVER, info.getCover());
        values.put(COLUMN_TIME, System.currentTimeMillis());
        return values;
    }

    public static ContentValues toContentValues(String historyName, String historyUrl) {
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
