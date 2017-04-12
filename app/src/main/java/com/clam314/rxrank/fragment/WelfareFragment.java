package com.clam314.rxrank.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam314.rxrank.MainApplication;
import com.clam314.rxrank.R;
import com.clam314.rxrank.adapter.LoadMoreViewHolder;
import com.clam314.rxrank.adapter.LoadMoreWrapperAdapter;
import com.clam314.rxrank.adapter.WelfareAdapter;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.presenter.DataPresenter;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class WelfareFragment extends CategoryFragment {
    private static final String ARG_PARAM2 = "PARAM2";
    private boolean isRandom = false;
    private boolean refreshing = false;

    @BindView(R.id.expanded_image) SimpleDraweeView svAnimation;
    @BindView(R.id.fl_content)View rootContent;
    public WelfareFragment() {
    }


    public static WelfareFragment newInstance(String category,boolean isRandom) {
        WelfareFragment fragment = new WelfareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, category);
        args.putBoolean(ARG_PARAM2, isRandom);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            isRandom = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welfare, container, false);
    }

    @Override
    protected void initView(View view) {
        moreAdapter =  new LoadMoreWrapperAdapter(new WelfareAdapter(mItems,svAnimation,rootContent));
        moreAdapter.setLoadStatusViewHolder(LoadMoreViewHolder.newInstance(getContext()),null,null);
        moreAdapter.setOnLoadListener(new LoadMoreWrapperAdapter.OnLoadListener() {
            @Override
            public void onRetry() {
                loadData(pageNo);
            }

            @Override
            public void onLoadMore() {
                loadData(pageNo);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(moreAdapter);
    }

    @Override
    protected void loadData(int page) {
        if(!isRandom){
            super.loadData(page);
        }else {
            loadRandomData();
        }
    }

    @Override
    public void onRefresh() {
        refreshing = true;
        super.onRefresh();
    }

    private void loadRandomData(){
        MainApplication.getInstance().getPresenter(DataPresenter.class).loadCategoryRandomContents(new Observer<List<Item>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<Item> items) {
                if(items == null || items.size() == 0 ){
                    moreAdapter.showLoadComplete();
                }else {
                    if(refreshing){
                        refreshing = false;
                        mItems.clear();
                    }
                    mItems.addAll(items);
                    moreAdapter.disableLoadMore();
                }
            }

            @Override
            public void onError(Throwable e) {
                moreAdapter.showLoadError();
            }

            @Override
            public void onComplete() {
            }

        },mCategory,20);
    }
}
