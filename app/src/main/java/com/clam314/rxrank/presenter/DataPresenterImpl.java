package com.clam314.rxrank.presenter;

import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.model.HttpModelPresenter;
import com.clam314.rxrank.model.HttpModelPresenterImpl;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
}
