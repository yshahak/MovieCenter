package com.thedroidboy.www.tmdblayer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.thedroidboy.www.tmdblayer.provider.MovieProvider;
import com.thedroidboy.www.tmdblayer.provider.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yshah on 1/19/2017.
 */

public class ServerHelper {


    public static final String GSON_KEY_Id = "id";
    public static final String GSON_KEY_TITLE = "title";
    public static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public static final String GSON_KEY_POSTER_PATH = "poster_path";
    public static final String GSON_KEY_OVERVIEW = "overview";
    public static final String GSON_KEY_RELEASE_DATE = "release_date";
    public static final String GSON_KEY_VOTE_AVERAGE = "vote_average";


    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    final static String APIKEY = "47a1ba7d133fe5e652e11a7812131fe0";
    public static final String MOVIE_POPULAR = "movie/popular";
    public static final String MOVIE_UPCOMING = "movie/upcoming";
    public static final String MOVIE_NOW_PLAYING = "movie/now_playing";

    public static final String LANGUAGE = "language";
    public static final String LANGUAGE_EN_US = "en-US";
    public static final String PAGE = "page";

    public final static String BASE_URL_SEARCH = "https://api.themoviedb.org/3/search/movie?";
    public static final String GSON_KEY_RESULTS = "results";

    public static String getPopularListUrl() {
        Uri.Builder uriBuilder = Uri.parse(TMDB_BASE_URL).buildUpon();
        uriBuilder.appendEncodedPath(MOVIE_POPULAR);
        uriBuilder.appendQueryParameter("api_key", APIKEY);
        uriBuilder.appendQueryParameter(LANGUAGE, LANGUAGE_EN_US);

        Uri builtUri = uriBuilder.build();
        return builtUri.toString();
    }

    public static String getqueryUrl(String query) {
        Uri.Builder uriBuilder = Uri.parse(BASE_URL_SEARCH).buildUpon();
        uriBuilder.appendQueryParameter("api_key", APIKEY);
        uriBuilder.appendQueryParameter("query", query);
        uriBuilder.appendQueryParameter(LANGUAGE, LANGUAGE_EN_US);
        Uri builtUri = uriBuilder.build();
        return builtUri.toString();
    }

    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static ContentValues[] convertJsonToMoviesList(Context context, String jsonString, String belongToList) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(GSON_KEY_RESULTS);
            List<ContentValues> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                String id = object.getString(GSON_KEY_Id);
                Cursor cursor = contentResolver.query(MoviesContract.MoviesEntry.CONTENT_URI,
                        new String[]{MoviesContract.MoviesEntry._ID},
                        MoviesContract.MoviesEntry._ID + " = " + id,
                        null,
                        null);
                if (cursor != null){
                    if (cursor.moveToFirst()) {
                        cursor.close();
                        continue;
                    }
                    cursor.close();
                }

                String movieTitle = object.getString(GSON_KEY_TITLE);
                String moviePoster = BASE_POSTER_URL + object.getString(GSON_KEY_POSTER_PATH);
                String movieOverview = object.getString(GSON_KEY_OVERVIEW);
                String releasedYear = object.getString(GSON_KEY_RELEASE_DATE);
                String rate = object.getString(GSON_KEY_VOTE_AVERAGE);
                ContentValues contentValues = MovieProvider.generateCntenetValue(id, movieTitle, moviePoster, movieOverview, releasedYear, rate);
                if (belongToList != null) {
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_LIST, belongToList);
                }
                list.add(contentValues);
            }
            return list.toArray(new ContentValues[list.size()]);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDataFromServer(String urlString){
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");

            }
            return buffer.toString();

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
        return null;
    }

}
