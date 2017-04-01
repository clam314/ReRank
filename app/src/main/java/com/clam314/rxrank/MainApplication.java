package com.clam314.rxrank;

import android.app.ActivityManager;
import android.app.Application;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatDelegate;

import com.clam314.rxrank.presenter.DataPresenter;
import com.clam314.rxrank.presenter.DataPresenterImpl;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.util.Map;

/**
 * Created by clam314 on 2017/3/31
 */

public class MainApplication extends Application {

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private volatile static MainApplication application;

    public static MainApplication getInstance(){
        return application;
    }

    private Map<Class<?>,Object> presenterMap;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initPresenter();
        initFresco();
    }

    private void initPresenter(){
        presenterMap = new ArrayMap<>();
        presenterMap.put(DataPresenter.class, new DataPresenterImpl());
    }

    private void initFresco() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig
                .newBuilder(getApplicationContext())
//                .setBitmapMemoryCacheParamsSupplier(new LollipopBitmapMemoryCacheParamsSupplier(activityManager))
                .build();

        Fresco.initialize(getApplicationContext(), imagePipelineConfig);
    }

    public <T> T getPresenter(Class<T> presenterClass){
        Object o = presenterMap.get(presenterClass);
        if(o == null){
            throw new IllegalArgumentException("presenter class "+ presenterClass.getName() +  " is not registered.");
        }else {
            return (T)o;
        }
    }
}
