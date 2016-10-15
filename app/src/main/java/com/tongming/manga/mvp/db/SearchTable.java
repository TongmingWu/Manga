package com.tongming.manga.mvp.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.tongming.manga.mvp.bean.SearchRecord;

import rx.functions.Func1;

/**
 * Author: Tongming
 * Date: 2016/10/10
 */

public class SearchTable {
    static final String TABLE_NAME = "search_record";

    static final String ID = "_id";
    static final String COLUMN_NAME = "comic_name";
    static final String COLUMN_URL = "comic_url";
    static final String COLUMN_SOURCE = "comic_source";
    static final String COLUMN_TIME = "last_time";

    static final String CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_SOURCE + " TEXT,"
            + COLUMN_URL + " VARCHAR(20),"
            + COLUMN_TIME + " INTEGER"
            + ")";

    static ContentValues toContentValues(SearchRecord record) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, record.getComic_name());
        values.put(COLUMN_URL, record.getComic_url());
        values.put(COLUMN_SOURCE, record.getComic_source());
        values.put(COLUMN_TIME, System.currentTimeMillis());
        return values;
    }

    static Func1<Cursor, SearchRecord> COMIC_MAPPER = new Func1<Cursor, SearchRecord>() {
        @Override
        public SearchRecord call(Cursor cursor) {
            SearchRecord record = new SearchRecord();
            record.setComic_name(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            record.setComic_source(cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE)));
            record.setComic_url(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
            record.setLast_time(cursor.getInt(cursor.getColumnIndex(COLUMN_TIME)));
            return record;
        }
    };
}
