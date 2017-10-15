package com.clam314.rxrank.model;

import com.clam314.rxrank.entity.zhihu.DailyNews;

import io.reactivex.Observable;

/**
 * Created by clam314 on 2017/10/15
 */

public interface ZhiHuHttpModelPresenter {
    Observable<DailyNews> loadDailyNews(String day);
}
