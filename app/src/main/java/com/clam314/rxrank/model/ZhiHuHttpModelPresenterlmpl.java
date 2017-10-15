package com.clam314.rxrank.model;

import com.clam314.rxrank.entity.zhihu.DailyNews;
import com.clam314.rxrank.entity.zhihu.Section;
import com.clam314.rxrank.entity.zhihu.Sections;
import com.clam314.rxrank.entity.zhihu.Theme;
import com.clam314.rxrank.entity.zhihu.Themes;
import com.clam314.rxrank.http.RetrofitUtil;
import com.clam314.rxrank.http.ZhiHuApi;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

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

    @Override
    public Observable<List<Theme>> loadThemeList() {
        return api.getThemeList().map(new Function<Themes, List<Theme>>() {
            @Override
            public List<Theme> apply(@NonNull Themes themes) throws Exception {
                return themes.getOthers();
            }
        });
    }

    @Override
    public Observable<List<Section>> loadSectionList() {
        return api.getSectionList().map(new Function<Sections, List<Section>>() {
            @Override
            public List<Section> apply(@NonNull Sections sections) throws Exception {
                return sections.getData();
            }
        });
    }
}
