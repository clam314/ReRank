package com.clam314.rxrank;

import android.app.ActivityManager;
import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by clam314 on 2017/3/31
 */

public class MainApplication extends Application {

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initFresco();
    }

    private void initFresco() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig
                .newBuilder(getApplicationContext())
//                .setBitmapMemoryCacheParamsSupplier(new LollipopBitmapMemoryCacheParamsSupplier(activityManager))
                .build();

        Fresco.initialize(getApplicationContext(), imagePipelineConfig);
    }
}
