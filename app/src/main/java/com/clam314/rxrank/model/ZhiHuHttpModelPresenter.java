package com.clam314.rxrank.model;

import com.clam314.rxrank.entity.zhihu.DailyNews;
import com.clam314.rxrank.entity.zhihu.Theme;
import com.clam314.rxrank.entity.zhihu.Themes;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by clam314 on 2017/10/15
 */

public interface ZhiHuHttpModelPresenter {
    Observable<DailyNews> loadDailyNews(String day);

    Observable<List<Theme>> loadThemeList();
}
