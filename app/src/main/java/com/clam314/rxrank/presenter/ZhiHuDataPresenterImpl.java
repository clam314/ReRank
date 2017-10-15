package com.clam314.rxrank.presenter;

import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.entity.zhihu.DailyNews;
import com.clam314.rxrank.model.ZhiHuHttpModelPresenter;
import com.clam314.rxrank.model.ZhiHuHttpModelPresenterlmpl;



import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by clam314 on 2017/10/15
 */

public class ZhiHuDataPresenterImpl implements ZhiHuDataPresenter {
    private ZhiHuHttpModelPresenter zhiHuHttpModelPresenter;

    public ZhiHuDataPresenterImpl() {
        zhiHuHttpModelPresenter = new ZhiHuHttpModelPresenterlmpl();
    }

    @Override
    public void loadNews(Observer<DailyNews> observer, String day) {
        Observable<DailyNews> observable = zhiHuHttpModelPresenter.loadDailyNews(day);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
