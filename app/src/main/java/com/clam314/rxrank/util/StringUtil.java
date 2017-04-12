package com.clam314.rxrank.util;


import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by clam314 on 2017/3/31
 */

public class StringUtil {
    private static SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
    private static SimpleDateFormat  destinationFormat = new SimpleDateFormat("yyyy/MM/dd",Locale.CHINA);

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

    public static String getStringBeforePosition(String s, int position){
        if(TextUtils.isEmpty(s)|| s.length() < position){
            return " ";
        }else {
            return s.substring(0,position);
        }
    }

    public static String getStringAfterPosition(String s,int position){
        if(TextUtils.isEmpty(s)|| s.length() < position){
            return " ";
        }else {
            return s.substring(position);
        }
    }

    /**
    * @param date 该字符串格式形如2016-03-05
    */
    public static String dateFormat(String date){
        try {
            return destinationFormat.format(sourceFormat.parse(date));
        }catch (ParseException e){
            e.printStackTrace();
            return "";
        }
    }
}
