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
import com.thedroidboy.www.tmdblayer.provider.MoviesContract;
import com.thedroidboy.www.tmdblayer.provider.MoviesDbHelper;
import com.thedroidboy.www.tmdblayer.provider.TestProviderUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        Cursor cursor = MovieStoreManager.getDefultMovieList(mContext);
        assertFalse(cursor.moveToFirst());
        ContentValues[] contentValues = TestProviderUtils.getHardCodeMovieArray();
        MovieStoreManager.insertListToProvider(mContext, contentValues);
        cursor = MovieStoreManager.getDefultMovieList(mContext);
        assertTrue(cursor.moveToFirst());
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
