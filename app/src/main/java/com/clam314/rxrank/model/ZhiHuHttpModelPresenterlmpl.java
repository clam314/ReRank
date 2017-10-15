package com.clam314.rxrank.model;

import com.clam314.rxrank.entity.zhihu.DailyNews;
import com.clam314.rxrank.http.RetrofitUtil;
import com.clam314.rxrank.http.ZhiHuApi;

import io.reactivex.Observable;

/**
 * Created by clam314 on 2017/10/15
 */

public class ZhiHuHttpModelPresenterlmpl implements ZhiHuHttpModelPresenter {
    private ZhiHuApi api;

    public ZhiHuHttpModelPresenterlmpl() {
        api = RetrofitUtil.getZhiHu().create(ZhiHuApi.class);
    }

    @Override
    public Observable<DailyNews> loadDailyNews(String day) {
        return api.getDailyNews(day);
    }
}
