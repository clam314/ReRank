package com.clam314.rerank.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.clam314.rerank.R;
import com.clam314.rerank.entity.HistoryDayResult;
import com.clam314.rerank.entity.Result;
import com.clam314.rerank.http.Category;
import com.clam314.rerank.http.HttpUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpUtil httpUtil = HttpUtil.getInstance();
        httpUtil.getHistoryDay(new Observer<HistoryDayResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HistoryDayResult historyDayResult) {
                if(historyDayResult != null){
//                    Log.d(TAG,historyDayResult.getResults().toString());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        httpUtil.getCategory(Category.android, 10, 1, new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result result) {
                if(result != null){
                    Log.d(TAG,"category: "+result.getResults().getAndroid().size());
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
