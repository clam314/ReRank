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
import com.clam314.rxrank.adapter.CategoryAdapter;
import com.clam314.rxrank.adapter.LoadMoreViewHolder;
import com.clam314.rxrank.adapter.LoadMoreWrapperAdapter;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.http.Category;
import com.clam314.rxrank.presenter.DataPresenter;
import com.clam314.rxrank.util.DeBugLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class CategoryFragment extends BaseFragment {
    protected static final String TAG = CategoryFragment.class.getSimpleName();
    protected static final String ARG_PARAM1 = "param1";
    protected static final String SAVE_ITEM_LIST = "mItems";
    protected static final String SAVE_PAGE_NO = "pageNo";
    protected String mCategory;
    protected ArrayList<Item> mItems;
    protected int pageNo = 1;

    @BindView(R.id.rv_category)
    RecyclerView recyclerView;

    protected LoadMoreWrapperAdapter moreAdapter;

    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(String category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCategory = getArguments().getString(ARG_PARAM1);
        }else {
            mCategory = Category.all;
        }
        if(savedInstanceState != null){
            mItems = savedInstanceState.getParcelableArrayList(SAVE_ITEM_LIST);
            pageNo = savedInstanceState.getInt(SAVE_PAGE_NO);
        }
        if(mItems == null){
            mItems = new ArrayList<>();
        }
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    protected void initView(View view) {
        moreAdapter = new LoadMoreWrapperAdapter(new CategoryAdapter(mItems));
        moreAdapter.setLoadStatusViewHolder(LoadMoreViewHolder.newInstance(view.getContext()),null,null);
        moreAdapter.setOnLoadListener(new LoadMoreWrapperAdapter.OnLoadListener() {
            @Override
            public void onRetry() {
                loadData(pageNo);
            }

            @Override
            public void onLoadMore() {
                DeBugLog.logDebug(TAG,mCategory+ " load data onLoadMore() page:"+ pageNo);
                loadData(pageNo);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(moreAdapter);
    }

    @Override
    protected void doAfterInitView(View view,@Nullable Bundle savedInstanceState) {
        DeBugLog.logWarning(TAG,mCategory+" doAfterInitView");
        if(savedInstanceState==null){
            loadData(pageNo);
        }
    }

    protected void loadData(final int page){
        MainApplication.getInstance().getPresenter(DataPresenter.class).loadCategoryContents(new Observer<List<Item>>() {
            @Override
            public void onSubscribe(Disposable d) {
                pageNo++;
            }

            @Override
            public void onNext(List<Item> items) {
                if(items == null || items.size() == 0 ){
                    moreAdapter.showLoadComplete();
                }else {
                    if(page == 1){
                        mItems.clear();
                    }
                    mItems.addAll(items);
                    moreAdapter.disableLoadMore();
                }
            }

            @Override
            public void onError(Throwable e) {
                pageNo--;
                moreAdapter.showLoadError();
            }

            @Override
            public void onComplete() {

            }

        },mCategory,10,page);
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        loadData(pageNo);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_ITEM_LIST,mItems);
        outState.putInt(SAVE_PAGE_NO,pageNo);
    }

}
