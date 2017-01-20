package com.thedroidboy.www.tmdblayer.provider;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by yshah on 1/19/2017.
 */

public class MovieProvider extends ContentProvider {

    private MoviesDbHelper mOpenHelper;

    private static final SQLiteQueryBuilder sQueryBuilder;

    static {
        sQueryBuilder = new SQLiteQueryBuilder();
        sQueryBuilder.setTables(MoviesContract.MoviesEntry.TABLE_NAME);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return MoviesContract.MoviesEntry.CONTENT_TYPE;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return mOpenHelper.getReadableDatabase().query(
                MoviesContract.MoviesEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, contentValues);
        if (_id > 0)
            returnUri = MoviesContract.MoviesEntry.buildMoviesUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) {
            selection = "1";
        }
        rowsDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;
        rowsUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection,
                selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    // This is a method specifically to assist the testing
    // framework in running smoothly.
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

    public Cursor getCursor(int projectid) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return db.rawQuery("select * from " + MoviesContract.MoviesEntry.TABLE_NAME
                + " where " + MoviesContract.MoviesEntry._ID + "='" + projectid + "'" , new String[]{MoviesContract.MoviesEntry._ID});
    }

    public static ContentValues generateCntenetValue(String id, String movieName, String moviePoster
            , String movieOverview, String releasedYear, String rate) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MoviesEntry._ID, id);
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME, movieName);
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_URL, moviePoster);
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW, movieOverview);
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_YEAR, releasedYear);
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RATE, rate);
        return values;
    }

    public static MovieItem convertRecordToMovieItem(Cursor moviesCursor) {
        return new MovieItem(
                moviesCursor.getInt(MoviesContract.MoviesEntry.COL_ID_INDEX)
                , moviesCursor.getString(MoviesContract.MoviesEntry.COL_MOVIE_NAME_INDEX)
                , moviesCursor.getString(MoviesContract.MoviesEntry.COL_MOVIE_OVERVIEW_INDEX)
                , moviesCursor.getString(MoviesContract.MoviesEntry.COL_MOVIE_POSTER_INDEX)
                , moviesCursor.getString(MoviesContract.MoviesEntry.COL_MOVIE_RELEASE_INDEX)
                , moviesCursor.getString(MoviesContract.MoviesEntry.COL_MOVIE_RATE_INDEX));
    }
}
