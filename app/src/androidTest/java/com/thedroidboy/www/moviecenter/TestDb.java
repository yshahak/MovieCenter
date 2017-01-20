package com.thedroidboy.www.moviecenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.thedroidboy.www.tmdblayer.provider.MoviesContract;
import com.thedroidboy.www.tmdblayer.provider.MoviesDbHelper;
import com.thedroidboy.www.tmdblayer.provider.TestProviderUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestDb {

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.thedroidboy.www.moviecenter", appContext.getPackageName());
    }

    @Test
    public void deleteTheDatabase() {
        assertTrue(InstrumentationRegistry.getTargetContext().deleteDatabase(MoviesDbHelper.DATABASE_NAME));
    }

    @Test
    public void testDbCreation(){
        Context mContext = InstrumentationRegistry.getTargetContext();
        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MoviesDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());
        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.MoviesEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());
        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(MoviesContract.MoviesEntry._ID);
        locationColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME);
        locationColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_URL);
        locationColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW);
        locationColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_YEAR);
        locationColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_MOVIE_RATE);
        locationColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_MOVIE_LIST);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        c.close();
        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required entry columns:" + locationColumnHashSet.toString(),
                locationColumnHashSet.isEmpty());
        db.close();
    }

    @Test
    public void testDbTable() {
        Context mContext = InstrumentationRegistry.getTargetContext();
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestProviderUtils.getHardCodeMovie();
        long locationRowId;
        locationRowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, testValues);
        assertTrue(locationRowId != -1);

        Cursor cursor = db.query(
                MoviesContract.MoviesEntry.TABLE_NAME,  // Table to Query
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue( "Error: No Records returned from query", cursor.moveToFirst() );

        cursor.close();
        db.close();

    }

}
