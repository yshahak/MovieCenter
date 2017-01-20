package com.thedroidboy.www.moviecenter;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by yshah on 1/19/2017.
 */

public class TestUtils {


    static void validateBulkCursor(String error, Cursor valueCursor, ContentValues[] expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        for (ContentValues contentValues : expectedValues  ) {
            validateCurrentRecord(error, valueCursor, contentValues);
        }
        valueCursor.close();
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    private static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}
