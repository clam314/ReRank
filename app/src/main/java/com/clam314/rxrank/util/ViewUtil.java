package com.clam314.rxrank.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.clam314.rxrank.activity.WebActivity;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.http.Category;

/**
 * Created by clam314 on 2017/4/3.
 */

public class ViewUtil {
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void statusBarCompat(Activity activity, View propUpView){
        if(Build.VERSION.SDK_INT >= 21){
            int statusBarHeight = getStatusBarHeight(activity);
            propUpView.setPadding(0,statusBarHeight,0,0);
        }
    }

    public static void setClickListenerOpenItem(View clickView, final Item item){
        if(item == null || TextUtils.isEmpty(item.getUrl())){
            Toast.makeText(clickView.getContext(),"数据有误,不能打开  :(",Toast.LENGTH_SHORT).show();
            clickView.setOnClickListener(null);
        }else {
            clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    if(Category.video.equals(item.getType())||ConfigUtil.isOpenPageBySystem()){
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(item.getUrl());
                        intent.setData(content_url);
                    }else {
                        intent.setClass(v.getContext(),WebActivity.class);
                        intent.putExtra(WebActivity.PARAM_ITEM,item);
                    }
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

}
