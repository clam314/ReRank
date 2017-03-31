package com.clam314.rxrank.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam314.rxrank.R;
import com.clam314.rxrank.adapter.CategoryAdapter;
import com.clam314.rxrank.adapter.LoadMoreWrapperAdapter;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.http.Category;
import com.clam314.rxrank.model.HttpModelPresenter;
import com.clam314.rxrank.model.HttpModelPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CategoryFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private String mCategory;
    private List<Item> mItems;

    @BindView(R.id.rv_category)
    RecyclerView recyclerView;

    private RecyclerView.Adapter moreAdapter;

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
        moreAdapter = new LoadMoreWrapperAdapter(getContext(),new CategoryAdapter(mItems));
        recyclerView.setAdapter(moreAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void doAfterInitView(View view) {
        loadData(0);
    }

    private void loadData(int page){
        HttpModelPresenter httpModelPresenter = new HttpModelPresenterImpl();
        Observable<List<Item>> observable = httpModelPresenter.loadCategory(mCategory,10,page);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Item>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Item> items) {
                        mItems.addAll(items);
                        moreAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
