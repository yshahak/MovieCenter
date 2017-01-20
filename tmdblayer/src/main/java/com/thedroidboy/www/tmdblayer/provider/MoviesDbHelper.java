package com.thedroidboy.www.tmdblayer.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yshah on 1/19/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "movies.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " ( " +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_URL + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_YEAR + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_RATE + " TEXT,  " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_LIST + " TEXT  " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
