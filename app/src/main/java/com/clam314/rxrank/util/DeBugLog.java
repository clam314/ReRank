package com.clam314.rxrank.util;

import android.util.Log;

import com.clam314.rxrank.BuildConfig;

/**
 * Created by clam314 on 2017/4/1
 */

public class DeBugLog {

    public static void logDebug(String TAG,String msg){
        if(BuildConfig.DEBUG)Log.d(TAG,msg);
    }

    public static void logInfo(String TAG,String msg){
        if(BuildConfig.DEBUG)Log.i(TAG,msg);
    }

    public static void logWarning(String TAG, String msg){
        if (BuildConfig.DEBUG)Log.w(TAG,msg);
    }

    public static void logError(String TAG,String msg){
        if(BuildConfig.DEBUG)Log.e(TAG,msg);
    }
}
