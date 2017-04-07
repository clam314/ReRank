package com.clam314.rxrank.model;

import com.clam314.rxrank.entity.CategoryGroup;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.http.Api;
import com.clam314.rxrank.http.HttpUtil;
import com.clam314.rxrank.http.RetrofitUtil;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by clam314 on 2017/3/31
 */

public class HttpModelPresenterImpl implements HttpModelPresenter{
    private Api mDataService;

    public HttpModelPresenterImpl(){
        mDataService = RetrofitUtil.getDefault().create(Api.class);
    }

    @Override
    public Observable<List<Item>> loadCategory(String category, int size, int page) {
        return HttpUtil.filterStatus(mDataService.getCategoryData(category,size,page));
    }

    @Override
    public Observable<List<String>> loadHistoryDays() {
        return HttpUtil.filterStatus(mDataService.getHistoryDay());
    }

    @Override
    public Observable<CategoryGroup> loadDay(String day) {
        return HttpUtil.filterStatus(mDataService.getDayData(day));
    }

    @Override
    public Observable<List<Item>> loadCategoryRandom(String category, int size) {
        return HttpUtil.filterStatus(mDataService.getRandomCategoryData(category,size));
    }

    @Override
    public Observable<ResponseBody> downloadFile(String url) {
        return mDataService.downlaodFile(url);
    }
}
