package com.clam314.rxrank;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatDelegate;

import com.clam314.rxrank.presenter.DataPresenter;
import com.clam314.rxrank.presenter.DataPresenterImpl;
import com.clam314.rxrank.util.ConfigUtil;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;
import java.util.Map;

/**
 * Created by clam314 on 2017/3/31
 */

public class MainApplication extends Application {

    static {
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
        initFresco(this,200);
        initTheme();
    }

    private void initTheme(){
        ConfigUtil.initConfig(this);
    }

    private void initPresenter(){
        presenterMap = new ArrayMap<>();
        presenterMap.put(DataPresenter.class, new DataPresenterImpl());
    }

    /*
    * 初始化操作，可在子线程操作
    *
    * */
    private void initFresco(final Context context, int cacheSizeInM) {
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setMaxCacheSize(cacheSizeInM*1024*1024)//最大缓存，单位M
                .setBaseDirectoryName("Photo_Fresco")//缓存所在的子目录名称
                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                    @Override
                    public File get() {
                        //推荐放在应用本身的缓存文件夹，卸载可清，其他清理软件也可扫描
                        return context.getCacheDir();
                    }
                }).build();
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                .setMainDiskCacheConfig(diskCacheConfig)
                //设置向下采样,处理速度比常规的scalling要快，同时支持gif/jpg/wep/png等，要配合ResizeOptions使用
                .setDownsampleEnabled(true)
                //设置bitmap的解码方式，这种模糊点，但省内存
                .setBitmapsConfig(Bitmap.Config.RGB_565)
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
