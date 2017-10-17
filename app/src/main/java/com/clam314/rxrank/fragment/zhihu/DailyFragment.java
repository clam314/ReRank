package com.clam314.rxrank.fragment.zhihu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clam314.rxrank.MainApplication;
import com.clam314.rxrank.R;
import com.clam314.rxrank.adapter.DailyAdapter;
import com.clam314.rxrank.adapter.LoadMoreViewHolder;
import com.clam314.rxrank.adapter.LoadMoreWrapperAdapter;
import com.clam314.rxrank.entity.zhihu.DailyNews;
import com.clam314.rxrank.entity.zhihu.Story;
import com.clam314.rxrank.fragment.BaseFragment;
import com.clam314.rxrank.presenter.ZhiHuDataPresenter;
import com.clam314.rxrank.util.DeBugLog;
import com.clam314.rxrank.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    @BindView(R.id.tv_day)
    TextView tvDate;
    private LoadMoreWrapperAdapter moreWrapperAdapter;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd",Locale.CHINA);
    private String queryDay;
    private String lastDay;
    private ArrayList<DailyNews> dataList;
    private ArrayList<Story> stories;


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
            stories = savedInstanceState.getParcelableArrayList(SAVE_DATA_LIST);
            queryDay = savedInstanceState.getString(SAVE_DAY_QUERY);
        }else {
            queryDay = lastDay;
            stories = new ArrayList<>();
        }

    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily,container,false);
    }

    @Override
    protected void initView(View view) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        manager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(manager);
        moreWrapperAdapter = new LoadMoreWrapperAdapter(new DailyAdapter(stories));
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
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int position = manager.findFirstVisibleItemPosition();
//                if(position>= 0 && position < dataList.size()){
//                    tvDate.setVisibility(View.VISIBLE);
//                    tvDate.setText(TimeUtil.timeForm(dataList.get(position).getDate(),"yyyyMMdd","MM/dd"));
//                }else {
//                    tvDate.setVisibility(View.GONE);
//                }
//            }
//        });
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
        outState.putParcelableArrayList(SAVE_DATA_LIST,stories);
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
                if(dailyNews != null && dailyNews.getStories() != null && !dailyNews.getStories().isEmpty()){
                    boolean refresh = false;
                    if(lastDay.equals(queryDay)){
                        refresh = true;
                        stories.clear();
                    }
                    List<Story> storyList = dailyNews.getStories();
                    if(!TextUtils.isEmpty(dailyNews.getDate())){
                        queryDay = dailyNews.getDate();
                        storyList.get(0).setShowDate(TimeUtil.timeForm(dailyNews.getDate(),"yyyyMMdd","MM/dd"));
                    }
                    int oldItemCount = stories.size();
                    stories.addAll(storyList);
                    if(refresh){
                        moreWrapperAdapter.disableLoadMore();
                    }else {
                        moreWrapperAdapter.disableLoadMore(oldItemCount,storyList.size());
                    }
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
