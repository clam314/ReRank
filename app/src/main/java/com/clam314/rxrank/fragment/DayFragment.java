package com.clam314.rxrank.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam314.rxrank.MainApplication;
import com.clam314.rxrank.R;
import com.clam314.rxrank.adapter.DayAdapter;
import com.clam314.rxrank.adapter.LoadMoreViewHolder;
import com.clam314.rxrank.adapter.LoadMoreWrapperAdapter;
import com.clam314.rxrank.entity.CategoryGroup;
import com.clam314.rxrank.http.Category;
import com.clam314.rxrank.presenter.DataPresenter;
import com.clam314.rxrank.util.DeBugLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class DayFragment extends BaseFragment {
    private static final String TAG = DayFragment.class.getSimpleName();

    private static final String SAVE_HISTORY_DAY_LIST = "day_list";
    private static final String SAVE_DAY_POSITION = "day_position";
    private static final String SAVE_DATA_LIST = "data_list";


    @BindView(R.id.rv_day)
    RecyclerView recyclerView;
    private LoadMoreWrapperAdapter moreWrapperAdapter;

    private ArrayList<String> historyDays;
    private int dayPosition = -1;
    private ArrayList<CategoryGroup> dataList;

    public DayFragment() {
    }

    public static DayFragment newInstance() {
        return new DayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            dataList = savedInstanceState.getParcelableArrayList(SAVE_DATA_LIST);
            historyDays = savedInstanceState.getStringArrayList(SAVE_HISTORY_DAY_LIST);
            dayPosition = savedInstanceState.getInt(SAVE_DAY_POSITION);
        }else {
            dataList = new ArrayList<>();
            historyDays = new ArrayList<>();
        }
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day,container,false);
    }

    @Override
    protected void initView(View view) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        moreWrapperAdapter = new LoadMoreWrapperAdapter(new DayAdapter(dataList));
        moreWrapperAdapter.setLoadStatusViewHolder(LoadMoreViewHolder.newInstance(getContext()),null,null);
        moreWrapperAdapter.setOnLoadListener(new LoadMoreWrapperAdapter.OnLoadListener() {
            @Override
            public void onRetry() {
                loadOneDayData(dayPosition);
            }

            @Override
            public void onLoadMore() {
                loadOneDayData(dayPosition);
            }
        });
        recyclerView.setAdapter(moreWrapperAdapter);
    }

    @Override
    protected void doAfterInitView(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadHistoryDay();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_DATA_LIST,dataList);
        outState.putStringArrayList(SAVE_HISTORY_DAY_LIST,historyDays);
        outState.putInt(SAVE_DAY_POSITION,dayPosition);
    }


    @Override
    public void onRefresh() {
        dayPosition = -1;
        loadHistoryDay();
    }

    private void loadOneDayData(final int position){
        if(historyDays == null ||position < 0|| historyDays.size() <= position){
            return;
        }
        final String day = historyDays.get(position);
        DeBugLog.logError(TAG,"loadOneDayData"+" day:"+day);
        MainApplication.getInstance().getPresenter(DataPresenter.class).loadDayContents(new Observer<CategoryGroup>() {
            @Override
            public void onSubscribe(Disposable d) {
                dayPosition++;
            }

            @Override
            public void onNext(CategoryGroup categoryGroup) {
                if(categoryGroup == null){
                    DeBugLog.logError(TAG,"loadOneDayData"+"onNext "+" showLoadComplete");
                    moreWrapperAdapter.showLoadComplete();
                }else {
                    if(position == 0){
                        dataList.clear();
                    }
                    categoryGroup.setDay(day);
                    dataList.add(categoryGroup);
                    moreWrapperAdapter.disableLoadMore();
                    DeBugLog.logError(TAG,"loadOneDayData"+"onNext"+" disableLoadMore"+ " -dataList:size:"+dataList.size()+" -day:"+day);
                }
            }

            @Override
            public void onError(Throwable e) {
                dayPosition--;
                moreWrapperAdapter.showLoadError();
            }

            @Override
            public void onComplete() {

            }
        },day);
    }

    private void loadHistoryDay(){
        MainApplication.getInstance().getPresenter(DataPresenter.class).loadHistoryDay(new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<String> strings) {
                historyDays.addAll(strings);
                if(historyDays != null && historyDays.size() > 0){
                    dayPosition = 0;
                }
                DeBugLog.logDebug(TAG,"historyDays:"+historyDays.size()+"-one:"+historyDays.get(0));
            }

            @Override
            public void onError(Throwable e) {
//                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                if(dayPosition > -1){
                    loadOneDayData(dayPosition);
                }
            }
        });
    }

}
