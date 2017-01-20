package com.thedroidboy.www.tmdblayer.provider;

import android.content.ContentValues;

/**
 * Created by yshah on 1/20/2017.
 */

public class TestProviderUtils {

    public static ContentValues[] getHardCodeMovieArray() {
        // Create a new map of values, where column names are the keys
        ContentValues[] values = new ContentValues[2];
        values[0] = generateCntenetValue("The Secret Life of Pets", "https://image.tmdb.org/t/p/w300_and_h450_bestv2/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg");
        values[1] = generateCntenetValue("Moana", "https://image.tmdb.org/t/p/w300_and_h450_bestv2/z4x0Bp48ar3Mda8KiPD1vwSY3D8.jpg");
        return values;
    }

    private static ContentValues generateCntenetValue(String movieName, String moviePoster) {
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME, movieName);
        testValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_URL, moviePoster);
        return testValues;
    }

    public static ContentValues getHardCodeMovie() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME, "The Secret Life of Pets");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_URL, "https://image.tmdb.org/t/p/w300_and_h450_bestv2/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg");
        return testValues;
    }
}
