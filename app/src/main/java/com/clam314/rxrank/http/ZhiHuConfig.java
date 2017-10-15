package com.clam314.rxrank.http;

/**
 * Created by clam314 on 2017/10/15
 */

public class ZhiHuConfig {
    static final String BASE_URL = "http://news.at.zhihu.com/api/4/";
    //获取新闻列表
    static final String NEWS = "news/before/{day}";
    //获取新闻额外信息
    static final String EXTRA_INFORMATION = "story-extra/{id}";
    //获取主题日报列表
    static final String THEMES_LIST = "themes";
    //获取主题日报内容
    static final String THEMES_CONTENT = "theme/{themeId}";
    //获取主题日报以往内容
    static final String THEMES_CONTENT_OLD = "theme/{themeId}/before/{id}";
    //获取栏目列表
    static final String SECTION_LIST = "sections";
    //获取栏目内容
    static final String SECTION_CONTENT = "section/{id}";
}
