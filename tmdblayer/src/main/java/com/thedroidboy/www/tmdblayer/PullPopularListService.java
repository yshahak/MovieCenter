package com.thedroidboy.www.tmdblayer;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.thedroidboy.www.tmdblayer.MovieStoreManager.CODE_POPULAR_LIST;
import static com.thedroidboy.www.tmdblayer.MovieStoreManager.EXTRA_LISTENER;
import static com.thedroidboy.www.tmdblayer.MovieStoreManager.insertListToProvider;
import static com.thedroidboy.www.tmdblayer.ServerHelper.MOVIE_POPULAR;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class PullPopularListService extends IntentService {


    public PullPopularListService() {
        super("PullPopularListService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        ResultReceiver callback = intent.getParcelableExtra(EXTRA_LISTENER);
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(ServerHelper.getPopularListUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");

            }
            String json = buffer.toString();
            ContentValues[] values = ServerHelper.convertJsonToMoviesList(this, json, MOVIE_POPULAR);
            if (values != null && values.length > 0) {
                insertListToProvider(this, values);
                if (callback != null) {
                    callback.send(CODE_POPULAR_LIST, null);
                }
            } else {
                Log.d("TAG", "no new data found");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
