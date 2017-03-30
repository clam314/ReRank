package com.clam314.rerank.http;

import com.clam314.rerank.entity.HistoryDayResult;
import com.clam314.rerank.entity.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by clam314 on 2017/3/30
 */

public interface Api {

    @GET(Config.historyDayUrl)
    public Observable<HistoryDayResult> getHistoryDay();

    @GET(Config.categoryUrl)
    public Observable<Result> getCategoryData(@Path("category") String category, @Path("size") int size, @Path("page") int page);

    @GET(Config.dayUrl)
    public Observable<Result> getDayData(@Path("day") String day);
}
