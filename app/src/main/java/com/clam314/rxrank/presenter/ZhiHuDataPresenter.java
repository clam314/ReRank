package com.clam314.rxrank.presenter;

import com.clam314.rxrank.entity.zhihu.DailyNews;
import com.clam314.rxrank.entity.zhihu.Section;
import com.clam314.rxrank.entity.zhihu.Theme;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by clam314 on 2017/10/15
 */

public interface ZhiHuDataPresenter {
    void loadNews(Observer<DailyNews> Observer, String day);

    void loadThemeList(Observer<List<Theme>> observer);

    void loadSectionList(Observer<List<Section>> observer);
}
