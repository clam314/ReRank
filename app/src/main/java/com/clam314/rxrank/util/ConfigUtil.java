package com.clam314.rxrank.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;

import com.clam314.rxrank.GlobalConfig;
import com.clam314.rxrank.R;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by clam314 on 2017/4/5
 */

public class ConfigUtil {
    private static final String showNightMode = "夜间模式";
    private static final String showNightModeNo = "日间模式";
    private static final String showNightModeAuto = "自动切换";

    private volatile static boolean openPageBySystem = false;

    public static boolean isOpenPageBySystem(){
        return openPageBySystem;
    }

    public static void initConfig(Context context){
        SharedPreferences sp = context.getSharedPreferences(GlobalConfig.NAME_PREFERENCE_SP,Context.MODE_PRIVATE);
        openPageBySystem = sp.getBoolean(context.getString(R.string.read_mode_key),false);
        final String mode = sp.getString(context.getString(R.string.setting_show_mode_key),showNightModeNo);
        Observable.just(mode)
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(@NonNull String s) throws Exception {
                        switch (s){
                            case showNightMode:return AppCompatDelegate.MODE_NIGHT_YES;
                            case showNightModeNo:return AppCompatDelegate.MODE_NIGHT_NO;
                            case showNightModeAuto:return AppCompatDelegate.MODE_NIGHT_AUTO;
                        }
                        return -1;
                    }
                }).filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return AppCompatDelegate.getDefaultNightMode() != integer && integer != -1;
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        AppCompatDelegate.setDefaultNightMode(integer);
                    }
                });
    }

    public static void setOpenPageBySystem(boolean isOpenPageBySystem){
         openPageBySystem = isOpenPageBySystem;
    }

    public static void changeNightMode(final Activity activity,final String mode){
        if(activity == null || TextUtils.isEmpty(mode)) return;
        Observable.just(mode)
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(@NonNull String s) throws Exception {
                        switch (s){
                            case showNightMode:return AppCompatDelegate.MODE_NIGHT_YES;
                            case showNightModeNo:return AppCompatDelegate.MODE_NIGHT_NO;
                            case showNightModeAuto:return AppCompatDelegate.MODE_NIGHT_AUTO;
                        }
                        return -1;
                    }
                }).filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return AppCompatDelegate.getDefaultNightMode() != integer && integer != -1;
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        AppCompatDelegate.setDefaultNightMode(integer);
                        activity.recreate();
                    }
                });
    }

    public static PackageInfo getVersionInfo(Context context){
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(),PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
