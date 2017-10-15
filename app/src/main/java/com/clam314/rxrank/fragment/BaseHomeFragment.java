package com.clam314.rxrank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam314.rxrank.R;
import com.clam314.rxrank.adapter.TabAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by clam314 on 2017/10/15
 */

public abstract class BaseHomeFragment extends BaseFragment {
    @BindView(R.id.vp_home)
    ViewPager viewPager;
    public TabLayout mTabLayout;
    private boolean isStart;

    private List<BaseFragment> fragments;

    public BaseHomeFragment() {

    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initView(View view) {
        List<String> titles = new ArrayList<>();
        fragments = new ArrayList<>();
        initFragmentList(view, titles, fragments);
        TabAdapter tabAdapter = new TabAdapter(titles, fragments, getChildFragmentManager());
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(0);
    }

    protected abstract void initFragmentList(View view, List<String> titles, List<BaseFragment> fragments);


    @Override
    public void onStart() {
        super.onStart();
        isStart = true;
        onChangeTabLayout();

    }

    @Override
    public void onStop() {
        super.onStop();
        isStart = false;
    }

    @Override
    public void onRefresh() {
        int position = viewPager.getCurrentItem();
        if (fragments != null && position < fragments.size()) {
            fragments.get(position).onRefresh();
        }
    }

    public void onChangeTabLayout() {
        if (!isStart) {
            return;
        }
        if (mTabLayout == null) {
            mTabLayout = (TabLayout) getActivity().findViewById(R.id.tabs_home);
        }
        mTabLayout.setupWithViewPager(viewPager);
        onChangeTabLayoutAfter(mTabLayout);
    }

    public void onChangeTabLayoutAfter(TabLayout tabLayout){
    }
}
