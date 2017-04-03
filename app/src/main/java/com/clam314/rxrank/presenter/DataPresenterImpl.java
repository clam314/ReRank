package com.clam314.rxrank.presenter;

import android.text.TextUtils;

import com.clam314.rxrank.entity.CategoryGroup;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.model.HttpModelPresenter;
import com.clam314.rxrank.model.HttpModelPresenterImpl;
import com.clam314.rxrank.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by clam314 on 2017/4/1
 */

public class DataPresenterImpl implements DataPresenter {
    private HttpModelPresenter httpModelPresenter;

    public DataPresenterImpl(){
        this.httpModelPresenter = new HttpModelPresenterImpl();
    }

    @Override
    public void loadCategoryContents(Observer<List<Item>> observer, String category, int size, int pageNo) {
        Observable<List<Item>> observable = httpModelPresenter.loadCategory(category,size,pageNo);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadHistoryDay(Observer<List<String>> observer) {
        Observable<List<String>> observable = httpModelPresenter.loadHistoryDays();
        observable.subscribeOn(Schedulers.io())
                .map(new Function<List<String>, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull List<String> strings) throws Exception {
                        List<String> des = new ArrayList<>();
                        for (String s:strings){
                            //将后台返回格式为yyyy-MM-dd的日期转换成yyyy/MM/dd的格式
                            des.add(StringUtil.dateFormat(s));
                        }
                        return des;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadDayContents(Observer<CategoryGroup> observer, String day) {
        Observable<CategoryGroup> observable = httpModelPresenter.loadDay(day);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadCategoryRandomContents(Observer<List<Item>> observer, String category, int size) {
        Observable<List<Item>> observable = httpModelPresenter.loadCategoryRandom(category,size);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
