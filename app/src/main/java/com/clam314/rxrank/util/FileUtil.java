package com.clam314.rxrank.util;


import android.os.Environment;
import android.text.TextUtils;
import com.clam314.rxrank.GlobalConfig;
import java.io.File;

/**
 * Created by clam314 on 2017/4/8
 */

public class FileUtil {

    public static String getAppExternalStorageDirectory(){
        File file = new File(Environment.getExternalStorageDirectory(), GlobalConfig.SD_SAVE_FILE_NAME);
        if(file.exists()){
            return file.getAbsolutePath();
        }else {
            if(file.mkdirs()){
                return file.getAbsolutePath();
            }else {
                return "";
            }
        }
    }

    public static String getFileSuffix(String fileName){
        if(TextUtils.isEmpty(fileName) || !fileName.contains(".")){
            return "";
        }else {
            return fileName.substring(fileName.lastIndexOf("."));
        }
    }
}
