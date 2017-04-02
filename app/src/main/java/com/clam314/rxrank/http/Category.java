package com.clam314.rxrank.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by clam314 on 2017/3/31
 */

public class Category {

    public static final String android = "Android";

    public static final String iOS = "iOS";

    public static final String welfare = "福利";

    public static final String frontEnd = "前端";

    public static final String app = "App";

    public static final String resource = "拓展资源";

    public static final String video = "休息视频";

    public static final String recommend = "瞎推荐";

    public static final String all = "all";

    private static final String[] categoryStrings = new String[]{android,iOS,welfare,frontEnd,app,resource,video,recommend};

    public static String[] getCategoryListBeside(final String... ss){
        final List<String> list = new ArrayList<>();
        Observable.fromArray(categoryStrings)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        for (String src : ss){
                            if(s.equals(src)) return false;
                        }
                        return true;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        list.add(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return list.toArray(new String[list.size()]);
    }
}
