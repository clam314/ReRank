package com.clam314.rxrank.util;



import android.system.ErrnoException;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by clam314 on 2017/10/15
 */

public class TimeUtil {

    public static String timeForm(String time, String formPattern, String toPattern){
        if(TextUtils.isEmpty(time)){
            return time;
        }else {
            try {
                SimpleDateFormat from = new SimpleDateFormat(formPattern,Locale.CHINA);
                SimpleDateFormat to = new SimpleDateFormat(toPattern, Locale.CHINA);
                return to.format(from.parse(time));
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }
        }
    }
}
