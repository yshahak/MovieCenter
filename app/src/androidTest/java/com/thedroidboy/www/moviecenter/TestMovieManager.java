package com.thedroidboy.www.moviecenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.thedroidboy.www.tmdblayer.MovieStoreManager;
import com.thedroidboy.www.tmdblayer.ServerHelper;
import com.thedroidboy.www.tmdblayer.provider.MoviesContract;
import com.thedroidboy.www.tmdblayer.provider.MoviesDbHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.thedroidboy.www.tmdblayer.MovieStoreManager.insertListToProvider;
import static com.thedroidboy.www.tmdblayer.ServerHelper.MOVIE_POPULAR;
import static junit.framework.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestMovieManager {



    @Test
    public void testDefaultList() {
        Context mContext = InstrumentationRegistry.getTargetContext();
        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);
        String json = ServerHelper.getDataFromServer(ServerHelper.getPopularListUrl());
        ContentValues[] values = ServerHelper.convertJsonToMoviesList(mContext, json, MOVIE_POPULAR);
        if (values != null && values.length > 0) {
            insertListToProvider(mContext, values);
            Cursor cursor = mContext.getContentResolver().query( MoviesContract.MoviesEntry.CONTENT_URI,
                    null,
                    MoviesContract.MoviesEntry.COLUMN_MOVIE_LIST + "=?",
                    new String[]{MOVIE_POPULAR},
                    null);
            assertTrue(cursor.moveToFirst());
            cursor.close();
        }

    }

    @Test
    public void testPopularUrl(){
        MovieStoreManager.pullPopularListFromServer(InstrumentationRegistry.getTargetContext()
                ,new ServiceResultReciever(new Handler(Looper.getMainLooper())));
    }

    @Test
    public void testQuery(){
        MovieStoreManager.searchServerForMovie(InstrumentationRegistry.getTargetContext()
                ,new ServiceResultReciever(new Handler(Looper.getMainLooper())), "Jack Reacher");
    }

    class ServiceResultReciever extends ResultReceiver {

        public ServiceResultReciever(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            Cursor cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, null, null, null, null);
            System.out.println(cursor.getCount());

        }
    }

}
