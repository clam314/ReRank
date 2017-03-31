package com.clam314.rerank.model;

import com.clam314.rerank.entity.Item;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by clam314 on 2017/3/31
 */

public interface HttpModelPresenter {

    Observable<List<Item>> loadCategory(String category, int size, int page);

    Observable<List<String>> loadHistoryDays();
}
