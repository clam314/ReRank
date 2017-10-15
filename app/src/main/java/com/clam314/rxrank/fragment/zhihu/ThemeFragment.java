package com.clam314.rxrank.fragment.zhihu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam314.rxrank.MainApplication;
import com.clam314.rxrank.R;
import com.clam314.rxrank.adapter.DayAdapter;
import com.clam314.rxrank.adapter.LoadMoreViewHolder;
import com.clam314.rxrank.adapter.LoadMoreWrapperAdapter;
import com.clam314.rxrank.adapter.ThemeAdapter;
import com.clam314.rxrank.entity.zhihu.Theme;
import com.clam314.rxrank.fragment.BaseFragment;
import com.clam314.rxrank.presenter.ZhiHuDataPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class ThemeFragment extends BaseFragment {
    private static final String SAVE_DATA_LIST = "DATA_LIST";
    @BindView(R.id.rv_theme)
    RecyclerView recyclerView;

    private LoadMoreWrapperAdapter moreWrapperAdapter;

    private ArrayList<Theme> dataList;
    public ThemeFragment() {
    }


    public static ThemeFragment newInstance() {
        ThemeFragment fragment = new ThemeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            dataList = savedInstanceState.getParcelableArrayList(SAVE_DATA_LIST);
        }else {
            dataList = new ArrayList<>();
        }

    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_theme,container,false);
    }

    @Override
    protected void initView(View view) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        moreWrapperAdapter = new LoadMoreWrapperAdapter(new ThemeAdapter(dataList));
        moreWrapperAdapter.setLoadStatusViewHolder(LoadMoreViewHolder.newInstance(getContext()),null,null);
        moreWrapperAdapter.setOnLoadListener(new LoadMoreWrapperAdapter.OnLoadListener() {
            @Override
            public void onRetry() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        recyclerView.setAdapter(moreWrapperAdapter);
    }

    @Override
    protected void doAfterInitView(View view, @Nullable Bundle savedInstanceState) {
        loadData();
    }

    private void loadData(){
        MainApplication.getInstance().getPresenter(ZhiHuDataPresenter.class).loadThemeList(new Observer<List<Theme>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Theme> list) {
                if(list != null && list.size() > 0){
                    dataList.clear();
                    dataList.addAll(list);
                    moreWrapperAdapter.showLoadError();
                }
            }

            @Override
            public void onError(Throwable e) {
                moreWrapperAdapter.showLoadError();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
