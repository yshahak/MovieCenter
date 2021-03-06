package com.thedroidboy.www.tmdblayer;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import static com.thedroidboy.www.tmdblayer.MovieStoreManager.CODE_QUERY;
import static com.thedroidboy.www.tmdblayer.MovieStoreManager.EXTRA_LISTENER;
import static com.thedroidboy.www.tmdblayer.MovieStoreManager.EXTRA_QUERY;
import static com.thedroidboy.www.tmdblayer.MovieStoreManager.insertListToProvider;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class QueryForMovieService extends IntentService {



    public QueryForMovieService() {
        super("PullPopularListService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        ResultReceiver callback = intent.getParcelableExtra(EXTRA_LISTENER);
        String query = intent.getStringExtra(EXTRA_QUERY);
        String json = ServerHelper.getDataFromServer(ServerHelper.getqueryUrl(query));
        ContentValues[] values = ServerHelper.convertJsonToMoviesList(this, json, query);
        if (values != null && values.length > 0) {
            insertListToProvider(this, values);
        }
        if (callback != null) {
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_QUERY, query);
            callback.send(CODE_QUERY, bundle);
        }
    }



}
