package com.clam314.rxrank.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam314.rxrank.R;
import com.clam314.rxrank.adapter.LoadMoreViewHolder;
import com.clam314.rxrank.adapter.LoadMoreWrapperAdapter;
import com.clam314.rxrank.adapter.WelfareAdapter;



public class WelfareFragment extends CategoryFragment {
    private static final String TAG = WelfareFragment.class.getSimpleName();

    public WelfareFragment() {
    }


    public static WelfareFragment newInstance(String category) {
        WelfareFragment fragment = new WelfareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, category);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welfare, container, false);
    }

    @Override
    protected void initView(View view) {
        moreAdapter =  new LoadMoreWrapperAdapter(new WelfareAdapter(mItems,this));
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
        super.loadData(page);
    }
}
