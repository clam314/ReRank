package com.clam314.rerank;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by clam314 on 2017/3/31
 */

public class MainApplication extends Application {

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
