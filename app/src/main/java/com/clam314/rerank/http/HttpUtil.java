package com.clam314.rerank.http;

import com.clam314.rerank.entity.HttpBean;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by clam314 on 2017/3/31
 */

public class HttpUtil {

    public static <T> Observable<T> filterStatus(Observable<HttpBean<T>> observable){
        return observable.map(new ResultFilter<T>());
    }

    private static class ResultFilter<T> implements Function<HttpBean<T>,T>{
        @Override
        public T apply(@NonNull HttpBean<T> tHttpBean) throws Exception {
            if(tHttpBean.isError()){
                throw new ApiException(true,tHttpBean.getMsg());
            }
            return tHttpBean.getData();
        }
    }
}
