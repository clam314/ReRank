package com.clam314.rxrank.model;

import android.content.Context;

import com.clam314.rxrank.entity.ImageCache;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by clam314 on 2017/4/7
 */

public interface PreferenceModePresenter {

    Observable<ImageCache> getHomeImageCache(Context context);

    void caveHomeImageCache(Context context, ImageCache cache);
}
