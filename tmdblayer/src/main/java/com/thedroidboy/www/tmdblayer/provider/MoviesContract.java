package com.thedroidboy.www.tmdblayer.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by yshah on 1/19/2017.
 */

public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "com.thedroidboy.www.tmdblayer.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().build();
        public static final String TABLE_NAME = "movies";
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_POSTER_URL = "poster_url";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_RELEASE_YEAR = "releas_year";
        public static final String COLUMN_MOVIE_RATE = "rate";
        public static final String COLUMN_MOVIE_LIST = "list";

        public static final int COL_ID_INDEX = 0;
        public static final int COL_MOVIE_NAME_INDEX = 1;
        public static final int COL_MOVIE_POSTER_INDEX = 2;
        public static final int COL_MOVIE_OVERVIEW_INDEX = 3;
        public static final int COL_MOVIE_RELEASE_INDEX = 4;
        public static final int COL_MOVIE_RATE_INDEX = 5;
        public static final int COL_LIST_INDEX = 6;


        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


}
