package com.clam314.rxrank.fragment.zhihu;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam314.rxrank.MainApplication;
import com.clam314.rxrank.R;
import com.clam314.rxrank.adapter.DailyAdapter;
import com.clam314.rxrank.adapter.LoadMoreViewHolder;
import com.clam314.rxrank.adapter.LoadMoreWrapperAdapter;
import com.clam314.rxrank.entity.zhihu.DailyNews;
import com.clam314.rxrank.fragment.BaseFragment;
import com.clam314.rxrank.presenter.ZhiHuDataPresenter;
import com.clam314.rxrank.util.DeBugLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class DailyFragment extends BaseFragment {
    private static final String TAG = DailyFragment.class.getSimpleName();
    private static final String SAVE_DAY_QUERY = "query_day";
    private static final String SAVE_DATA_LIST = "data_list";
    @BindView(R.id.rv_daily)
    RecyclerView recyclerView;
    private LoadMoreWrapperAdapter moreWrapperAdapter;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd",Locale.CHINA);
    private String queryDay;
    private String lastDay;
    private ArrayList<DailyNews> dataList;

    public DailyFragment() {
    }


    public static DailyFragment newInstance() {
        DailyFragment fragment;
        fragment = new DailyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK,1);
        lastDay = dateFormat.format(calendar.getTime());
        if(savedInstanceState != null){
            dataList = savedInstanceState.getParcelableArrayList(SAVE_DATA_LIST);
            queryDay = savedInstanceState.getString(SAVE_DAY_QUERY);
        }else {
            queryDay = lastDay;
            dataList = new ArrayList<>();
        }

    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily,container,false);
    }

    @Override
    protected void initView(View view) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        moreWrapperAdapter = new LoadMoreWrapperAdapter(new DailyAdapter(dataList));
        moreWrapperAdapter.setLoadStatusViewHolder(LoadMoreViewHolder.newInstance(getContext()),null,null);
        moreWrapperAdapter.setOnLoadListener(new LoadMoreWrapperAdapter.OnLoadListener() {
            @Override
            public void onRetry() {
                loadDailyNews(queryDay);
            }

            @Override
            public void onLoadMore() {
                DeBugLog.logInfo(TAG,"onRetry:"+queryDay);
                loadDailyNews(queryDay);
            }
        });
        recyclerView.setAdapter(moreWrapperAdapter);
    }

    @Override
    protected void doAfterInitView(View view, @Nullable Bundle savedInstanceState) {
        loadDailyNews(queryDay);
    }

    @Override
    public void onRefresh() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK,1);
        queryDay = lastDay = dateFormat.format(calendar.getTime());
        loadDailyNews(queryDay);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_DATA_LIST,dataList);
        outState.putString(SAVE_DAY_QUERY,queryDay);
    }

    private void loadDailyNews(final String day){
        DeBugLog.logInfo(TAG,"loadDailyNews:"+day);
        MainApplication.getInstance().getPresenter(ZhiHuDataPresenter.class).loadNews(new Observer<DailyNews>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DailyNews dailyNews) {
                if(dailyNews != null){
                    if(lastDay.equals(queryDay)){
                        dataList.clear();
                    }
                    if(!TextUtils.isEmpty(dailyNews.getDate())){
                        queryDay = dailyNews.getDate();
                    }
                    dataList.add(dailyNews);
                    moreWrapperAdapter.disableLoadMore();
                }else {
                    moreWrapperAdapter.showLoadComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                moreWrapperAdapter.showLoadError();
            }

            @Override
            public void onComplete() {

            }
        },day);
    }

}
