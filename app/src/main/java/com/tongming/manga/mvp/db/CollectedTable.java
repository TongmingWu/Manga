package com.tongming.manga.mvp.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.ComicInfo;

import rx.functions.Func1;

/**
 * Created by Tongming on 2016/8/17.
 */
public class CollectedTable {
    public static final String TABLE_NAME = "collect_comic";

    // 表字段
    public static final String ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_AREA = "area";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_COVER = "cover";
    public static final String COLUMN_TIME = "last_time";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_AUTHOR + " TEXT,"
            + COLUMN_AREA + " TEXT,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_URL + " VARCHAR(20),"
            + COLUMN_STATUS + " INTEGER,"
            + COLUMN_COVER + " VARCHAR(60),"
            + COLUMN_TIME + " INTEGER"
            + ")";

    // 对象转字段,放入表中
    public static ContentValues toContentValues(ComicInfo info) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, info.getComic_name());
        values.put(COLUMN_AUTHOR, info.getComic_author());
        values.put(COLUMN_AREA, info.getComic_area());
        values.put(COLUMN_TYPE, info.getComic_type());
        values.put(COLUMN_URL, info.getComic_url());
        values.put(COLUMN_STATUS, info.getStatus().contains("连载") ? 0 : 1);
        values.put(COLUMN_COVER, info.getCover());
        values.put(COLUMN_TIME, System.currentTimeMillis());
        return values;
    }

    // 响应式的查询,根据表中的row生成一个对象
    static Func1<Cursor, CollectedComic> COMIC_MAPPER = new Func1<Cursor, CollectedComic>() {
        @Override
        public CollectedComic call(Cursor cursor) {
            CollectedComic comic = new CollectedComic();
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
            int status = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS));
            comic.setStatus(status);
            String cover = cursor.getString(cursor.getColumnIndex(COLUMN_COVER));
            comic.setCover(cover);
            long lastTime = cursor.getLong(cursor.getColumnIndex(COLUMN_TIME));
            comic.setLastTime(lastTime);
            return comic;
        }
    };

}
