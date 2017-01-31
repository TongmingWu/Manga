package com.tongming.manga.mvp.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.ComicInfo;

import rx.functions.Func1;

/**
 * Created by Tongming on 2016/8/17.
 */
class CollectionTable {
    static final String TABLE_NAME = "collect_comic";

    // 表字段
    static final String ID = "_id";
    static final String COMIC_ID = "comic_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_AUTHOR = "author";
    static final String COLUMN_AREA = "area";
    static final String COLUMN_TYPE = "category";
    static final String COLUMN_SOURCE = "source";
    static final String COLUMN_URL = "url";
    static final String COLUMN_STATUS = "status";
    static final String COLUMN_COVER = "cover";
    static final String COLUMN_TIME = "last_time";

    static final String CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COMIC_ID + " TEXT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_AUTHOR + " TEXT,"
            + COLUMN_AREA + " TEXT,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_URL + " VARCHAR(50),"
            + COLUMN_STATUS + " INTEGER,"
            + COLUMN_COVER + " VARCHAR(120),"
            + COLUMN_SOURCE + " TEXT,"
            + COLUMN_TIME + " INTEGER"
            + ")";

    // 对象转字段,放入表中
    static ContentValues toContentValues(ComicInfo info) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, info.getComic_name());
        values.put(COMIC_ID, info.getComic_id());
        values.put(COLUMN_AUTHOR, info.getComic_author());
        values.put(COLUMN_AREA, info.getComic_area());
        values.put(COLUMN_SOURCE, info.getComic_source());
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
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            comic.setComic_name(name);
            String comicId = cursor.getString(cursor.getColumnIndex(COMIC_ID));
            comic.setComic_id(comicId);
            String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
            comic.setComic_author(author);
            comic.setComic_source(cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE)));
            String area = cursor.getString(cursor.getColumnIndex(COLUMN_AREA));
            comic.setComic_area(area);
            String category = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
            comic.setComic_category(category);
            String url = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
            comic.setComic_url(url);
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
