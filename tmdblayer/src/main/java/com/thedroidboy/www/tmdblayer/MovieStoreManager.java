package com.thedroidboy.www.tmdblayer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.ResultReceiver;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.thedroidboy.www.tmdblayer.provider.MoviesContract;

import static com.thedroidboy.www.tmdblayer.ServerHelper.MOVIE_POPULAR;

/**
 * Created by yshah on 1/19/2017.
 */

public class MovieStoreManager {
    public static final String EXTRA_LISTENER = "extraListener";
    public static final String EXTRA_QUERY = "extraQuery";
    public static final int CODE_POPULAR_LIST = 100;
    public static final int CODE_QUERY = 101;


    /*
    This method will return the popular movies list
     */
    public static Loader<Cursor> getDefultMovieList(Context context){
        return new CursorLoader(context,
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_LIST + "=?",
                new String[]{MOVIE_POPULAR},
                null);
    }

    public static Loader<Cursor> getQueryResults(Context context, String query){
        return new CursorLoader(context,
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_LIST + "=?",
                new String[]{query},
                null);
    }


    /*
    Store the pulled popular list in the moviesProvider
     */
    public static int insertListToProvider(Context mContext, ContentValues[] contentValues){
        return mContext.getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);
    }

    /*
    pull a popular movies list from the TMDB server
     */
    public static void pullPopularListFromServer(Context context, ResultReceiver listener){
        Intent intent = new Intent(context, PullPopularListService.class);
        intent.putExtra(EXTRA_LISTENER, listener);
        context.startService(intent);
}

    public static void searchServerForMovie(Context context, ResultReceiver listener, String query){
        Intent intent = new Intent(context, QueryForMovieService.class);
        intent.putExtra(EXTRA_LISTENER, listener);
        intent.putExtra(EXTRA_QUERY, query);
        context.startService(intent);
    }

}
