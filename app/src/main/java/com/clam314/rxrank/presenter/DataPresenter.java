package com.clam314.rxrank.presenter;

import com.clam314.rxrank.entity.Item;

import java.util.List;

import io.reactivex.Observer;

/**
 * Created by clam314 on 2017/3/31
 */

public interface DataPresenter {

    void loadCategoryContents(Observer<List<Item>> observer, String category, int size, int pageNo);

}
