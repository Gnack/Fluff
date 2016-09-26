package com.ru.gacklash.fluff.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.ru.gacklash.fluff.support.BookmarkContract.*;

/**
 * Created by gnack_000 on 30.03.2016.
 */
public class BookmarkDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION=2;

    static final String DB_NAME="fluff.db";
    public BookmarkDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_BOOKMARK_TABLE = "CREATE TABLE " + BookmarkEntry.TABLE_NAME + " (" +
                BookmarkEntry._ID + " INTEGER PRIMARY KEY," +
                BookmarkEntry.COLUMN_BOOKMARK_ADDRESS + " TEXT NOT NULL, " +
                BookmarkEntry.COLUMN_BOOKMARK_PROGRESS + " TEXT NOT NULL, " +
                BookmarkEntry.COLUMN_BOOKMARK_NAME + " TEXT NOT NULL " +
                " );";
        db.execSQL(SQL_CREATE_BOOKMARK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BookmarkEntry.TABLE_NAME);
        onCreate(db);
    }

}
