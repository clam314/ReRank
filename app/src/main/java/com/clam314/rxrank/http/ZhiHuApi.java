package com.clam314.rxrank.http;

import com.clam314.rxrank.entity.zhihu.DailyNews;
import com.clam314.rxrank.entity.zhihu.ThemeContent;
import com.clam314.rxrank.entity.zhihu.Themes;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by clam314 on 2017/10/15
 */

public interface ZhiHuApi {

    @GET(ZhiHuConfig.NEWS)
    Observable<DailyNews> getDailyNews(@Path("day") String day);

    @GET(ZhiHuConfig.THEMES_LIST)
    Observable<Themes> getThemeList();

    @GET(ZhiHuConfig.THEMES_CONTENT)
    Observable<ThemeContent> getThemeContent(@Path("themeId") String themeId);

    @GET(ZhiHuConfig.THEMES_CONTENT_OLD)
    Observable<ThemeContent> getThemeContent(@Path("themeId") String themeId, @Path("id") String id);
}
