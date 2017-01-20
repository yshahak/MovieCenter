package com.thedroidboy.www.moviecenter.control;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.v4.content.Loader;

import com.thedroidboy.www.moviecenter.ui.MoviesListActivity;
import com.thedroidboy.www.tmdblayer.MovieStoreManager;
import com.thedroidboy.www.tmdblayer.ServerHelper;

import java.lang.ref.WeakReference;

/**
 * Created by yshah on 1/19/2017.
 */

public class MoviesController {

    private static MoviesController instance = null;
    private static WeakReference<MoviesListActivity> mWeakRefernce;
    private ServiceResultReciever resultReciever = new ServiceResultReciever(new Handler(Looper.getMainLooper()));


    public static MoviesController getInstance() {
        if(instance == null) {
            instance = new MoviesController();
        }
        return instance;
    }

    /*
        get popular movies list from the Movie Store
     */
    public Loader<Cursor> getPopularMovieList(MoviesListActivity context){
        mWeakRefernce = new WeakReference<>(context);
        refreshDataBase(context);
        return MovieStoreManager.getDefultMovieList(context);
    }

    /*
        get popular movies list from the Movie Store
     */
    public Loader<Cursor> getQueryResults(MoviesListActivity context, String query){
        mWeakRefernce = new WeakReference<>(context);
        return MovieStoreManager.getQueryResults(context, query);
    }


    /**
     * asynchronic query server for a word
     */
    public void queryServer(MoviesListActivity context, String query){
        mWeakRefernce = new WeakReference<>(context);
        MovieStoreManager.searchServerForMovie(context, resultReciever, query);
    }

    /**
     * refresh the data in the database
     * @param context context
     */
    private void refreshDataBase(Context context){
        if (ServerHelper.isNetworkAvailable(context)) {
            MovieStoreManager.pullPopularListFromServer(context, resultReciever);
        }
    }


    /**
     *  classs to get the results from the service and publish in the UI
     */
    private class ServiceResultReciever extends ResultReceiver{

        ServiceResultReciever(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            MoviesListActivity itemListActivity = mWeakRefernce.get();
            if (itemListActivity != null) {
                itemListActivity.getSupportLoaderManager().restartLoader(resultCode, resultData, itemListActivity);
            }

        }
    }
}
