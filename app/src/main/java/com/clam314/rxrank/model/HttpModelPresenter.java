package com.clam314.rxrank.model;

import com.clam314.rxrank.entity.CategoryGroup;
import com.clam314.rxrank.entity.Item;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by clam314 on 2017/3/31
 */

public interface HttpModelPresenter {

    Observable<List<Item>> loadCategory(String category, int size, int page);

    Observable<List<String>> loadHistoryDays();

    Observable<CategoryGroup> loadDay(String day);

    Observable<List<Item>> loadCategoryRandom(String category,int size);

    Observable<ResponseBody> downloadFile(String url);
}
