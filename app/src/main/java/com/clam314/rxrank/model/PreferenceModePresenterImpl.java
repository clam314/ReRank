package com.clam314.rxrank.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.clam314.rxrank.entity.ImageCache;
import com.clam314.rxrank.util.DeBugLog;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by clam314 on 2017/4/7
 */

public class PreferenceModePresenterImpl implements PreferenceModePresenter {

    private static final String SP_HOME = "home_share_preference";
    private static final String SP_HOME_REFRESH_TIME = "home_refresh_time";
    private static final String SP_HOME_IMAGE_SAVE_PATH = "home_save_path";

    @Override
    public Observable<ImageCache> getHomeImageCache(final Context context) {
        return new Observable<ImageCache>(){
            @Override
            protected void subscribeActual(Observer<? super ImageCache>observer) {
                if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                    observer.onError(new Throwable("SD card status is not MEDIA_MOUNTED"));
                    return;
                }
                SharedPreferences sharedPreferences = context.getSharedPreferences(SP_HOME,Context.MODE_PRIVATE);
                long refreshTime = sharedPreferences.getLong(SP_HOME_REFRESH_TIME,0);
                String savePath = sharedPreferences.getString(SP_HOME_IMAGE_SAVE_PATH,"");
                ImageCache cache = new ImageCache();
                cache.setRefreshTime(refreshTime);
                cache.setSavePath(savePath);
                observer.onNext(cache);
                observer.onComplete();
            }
        };
    }

    @Override
    public void caveHomeImageCache(Context context,ImageCache cache) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_HOME,Context.MODE_PRIVATE);
        String currentFile = sharedPreferences.getString(SP_HOME_IMAGE_SAVE_PATH,"");
        File old = new File(currentFile);
        if(old.exists()){
            if(old.delete())DeBugLog.logDebug("ImageCache","old Image cache has been deleted");
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(SP_HOME_REFRESH_TIME,cache.getRefreshTime());
        editor.putString(SP_HOME_IMAGE_SAVE_PATH,cache.getSavePath());
        editor.apply();
    }
}
