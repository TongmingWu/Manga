package com.tongming.manga.mvp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

/**
 * Created by Tongming on 2016/8/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "comic.db";
    private static final int DB_VERSION = 2;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HistoryTable.CREATE);
        db.execSQL(CollectionTable.CREATE);
        db.execSQL(DownloadTable.CREATE);
        db.execSQL(SearchTable.CREATE);
        Logger.d("数据库创建完成");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 一般在实际项目中是不能这么做的，正确的做法是在更新数据表结构时，还要考虑用户存放于数据库中的数据不丢失
        db.execSQL("DROP TABLE IF EXISTS " + HistoryTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CollectionTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DownloadTable.TABLE_NAME);
        onCreate(db);
        Logger.d("更新数据库");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //在数据库打开时执行
    }
}
