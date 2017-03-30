package com.clam314.rerank.http;

import com.clam314.rerank.entity.HistoryDayResult;
import com.clam314.rerank.entity.Result;


import java.util.List;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by clam314 on 2017/3/31
 */

public class HttpUtil {
    private volatile static HttpUtil httpUtil;

    private Api api;
    private Retrofit retrofit;

    private HttpUtil(){
        retrofit = RetrofitUtil.getDefault();
        api = retrofit.create(Api.class);
    }

    public static HttpUtil getInstance(){
        if(httpUtil == null){
            synchronized (HttpUtil.class){
                httpUtil = new HttpUtil();
            }
        }
        return httpUtil;
    }

    public void getHistoryDay(Observer<HistoryDayResult> observer){
        api.getHistoryDay()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getCategory(String category, int size, int page, Observer<Result> observer){
        api.getCategoryData(category,size,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
