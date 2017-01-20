package com.thedroidboy.www.moviecenter;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.thedroidboy.www.tmdblayer.provider.MovieProvider;
import com.thedroidboy.www.tmdblayer.provider.MoviesContract;
import com.thedroidboy.www.tmdblayer.provider.MoviesDbHelper;
import com.thedroidboy.www.tmdblayer.provider.TestProviderUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by yshah on 1/19/2017.
 */

@RunWith(AndroidJUnit4.class)
public class TestMoviesProvider {

    @Test
    public void testProviderRegistry() {
        Context mContext = InstrumentationRegistry.getTargetContext();

        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MoviesContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MoviesContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    @Test
    public void deleteTheDatabase() {
        Context mContext = InstrumentationRegistry.getTargetContext();
        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);

    }

    @Test
    public void testCreation(){
        Context mContext = InstrumentationRegistry.getTargetContext();
        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);

        ContentValues[] contentValues = TestProviderUtils.getHardCodeMovieArray();

        long rowCount = mContext.getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);
        assertTrue("Unable to Insert MovieEntry into the Database", rowCount == contentValues.length);
        // Test the basic movieTitle provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        assertTrue(movieCursor.getCount() == contentValues.length);
        movieCursor.close();
    }

    @Test
    public void testQuery(){
        Context mContext = InstrumentationRegistry.getTargetContext();

        // Test the basic movieTitle provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertTrue(movieCursor.moveToFirst());
        movieCursor.close();

    }

}
