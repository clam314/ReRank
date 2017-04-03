package com.clam314.rxrank.http;

/**
 * Created by clam314 on 2017/3/30
 */

public class Config {

    public static final String baseUrl = "http://gank.io/api/";

    public static final String historyDayUrl = "day/history";

    public static final String categoryUrl = "data/{category}/{size}/{page}";//该接口page从1开始

    public static final String dayUrl = "day/{day}";

    public static final String randomCategoryUrl = "random/data/{category}/{size}";
}
