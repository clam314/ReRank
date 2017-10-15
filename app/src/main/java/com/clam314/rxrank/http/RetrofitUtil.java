package com.clam314.rxrank.http;

import com.clam314.rxrank.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by clam314 on 2017/3/30
 */

public class RetrofitUtil {
    private volatile static Retrofit defaultRetrofit;
    private volatile static Retrofit zhiHuRetrofit;

    public static Retrofit getDefault(){
        if(defaultRetrofit == null){
            synchronized (RetrofitUtil.class){
                OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
                //设置连接的超时时间
                builder.readTimeout(10, TimeUnit.SECONDS);
                builder.connectTimeout(10,TimeUnit.SECONDS);

                if(BuildConfig.DEBUG){
                    //HttpLoggingInterceptor 是一个拦截器，用于输出网络请求和结果的Log
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    builder.addInterceptor(interceptor);
                }

                defaultRetrofit = new Retrofit.Builder()
                        .baseUrl(Config.baseUrl)
                        .client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }
        }
        return defaultRetrofit;
    }

    public static Retrofit getZhiHu() {
        if(zhiHuRetrofit == null){
            synchronized (RetrofitUtil.class){
                OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
                //设置连接的超时时间
                builder.readTimeout(10, TimeUnit.SECONDS);
                builder.connectTimeout(10,TimeUnit.SECONDS);

                if(BuildConfig.DEBUG){
                    //HttpLoggingInterceptor 是一个拦截器，用于输出网络请求和结果的Log
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    builder.addInterceptor(interceptor);
                }

                zhiHuRetrofit = new Retrofit.Builder()
                        .baseUrl(ZhiHuConfig.BASE_URL)
                        .client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }
        }
        return zhiHuRetrofit;
    }

}
