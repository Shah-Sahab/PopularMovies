package com.popularmovies.base;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Psych on 12/24/16.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
