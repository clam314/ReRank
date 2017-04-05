package com.clam314.rxrank;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by clam314 on 2017/4/5
 */

public class GlobalConfig {
    public static final String NAME_PREFERENCE_SP = "MY_PREFERENCE";

    public static void setNightModeTheme(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    }
}
