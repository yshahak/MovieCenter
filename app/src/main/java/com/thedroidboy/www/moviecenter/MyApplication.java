package com.thedroidboy.www.moviecenter;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;

/**
 * Created by yshah on 1/20/2017.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        Picasso picasso = Picasso.with(this);
        picasso.setLoggingEnabled(false);

    }
}
