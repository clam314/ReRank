package com.clam314.rxrank.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
    private static final String TAG = CategoryFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private String mCategory;
    private List<Item> mItems;
    private int pageNo = 0;

    @BindView(R.id.rv_category)
    RecyclerView recyclerView;

    private LoadMoreWrapperAdapter moreAdapter;

    private OnFragmentInteractionListener mListener;

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
        mItems = new ArrayList<>();
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
                DeBugLog.logDebug(TAG,"load data onLoadMore() page:"+ pageNo);
                loadData(pageNo);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(moreAdapter);
    }

    @Override
    protected void doAfterInitView(View view) {
        loadData(pageNo);
    }

    private void loadData(final int page){
        MainApplication.getInstance().getPresenter(DataPresenter.class).loadCategoryContents(new Observer<List<Item>>() {
            @Override
            public void onSubscribe(Disposable d) {
                pageNo++;
                DeBugLog.logDebug(TAG,"load data onSubscribe() page:"+ pageNo);
            }

            @Override
            public void onNext(List<Item> items) {
                if(items == null || items.size() == 0 ){
                    moreAdapter.showLoadComplete();
                    DeBugLog.logDebug(TAG,"load data showLoadComplete() page:"+ pageNo);
                }else {
                    mItems.addAll(items);
                    moreAdapter.disableLoadMore();
                    DeBugLog.logDebug(TAG,"load data disableLoadMore() page:"+ pageNo);
                }
            }

            @Override
            public void onError(Throwable e) {
                pageNo--;
                moreAdapter.showLoadError();
                DeBugLog.logError(TAG,"load data error: "+  e.getMessage());
            }

            @Override
            public void onComplete() {
                DeBugLog.logDebug(TAG,"load data onComplete() page:"+ pageNo);
            }

        },mCategory,10,page);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
