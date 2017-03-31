package com.clam314.rxrank.util;

import android.text.TextUtils;

/**
 * Created by clam314 on 2017/3/31
 */

public class StringUtil {

    public static String getShowStringNotNull(String s){
        if(TextUtils.isEmpty(s)){
            return " ";
        }else{
            return s;
        }
    }

    public static String getCharFromString(String s,int position){
        if(TextUtils.isEmpty(s)|| s.length() < position + 1){
            return " ";
        }else {
            return String.valueOf(s.charAt(position));
        }
    }
}
