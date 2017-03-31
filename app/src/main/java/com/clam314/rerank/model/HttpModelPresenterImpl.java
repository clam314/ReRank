package com.clam314.rerank.model;

import com.clam314.rerank.entity.Item;
import com.clam314.rerank.http.Api;
import com.clam314.rerank.http.HttpUtil;
import com.clam314.rerank.http.RetrofitUtil;

import java.util.List;

import io.reactivex.Observable;

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
}
