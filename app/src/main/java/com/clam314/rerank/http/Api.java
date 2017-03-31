package com.clam314.rerank.http;

import com.clam314.rerank.entity.CategoryGroup;
import com.clam314.rerank.entity.HttpBean;
import com.clam314.rerank.entity.Item;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by clam314 on 2017/3/30
 */

public interface Api {

    @GET(Config.historyDayUrl)
    public Observable<HttpBean<List<String>>> getHistoryDay();

    @GET(Config.categoryUrl)
    public Observable<HttpBean<List<Item>>> getCategoryData(@Path("category") String category, @Path("size") int size, @Path("page") int page);

    @GET(Config.dayUrl)
    public Observable<HttpBean<CategoryGroup>> getDayData(@Path("day") String day);
}
