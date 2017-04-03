package com.clam314.rxrank.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam314.rxrank.R;
import com.clam314.rxrank.adapter.TabAdapter;
import com.clam314.rxrank.http.Category;

import java.util.ArrayList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends BaseFragment {

    @BindView(R.id.vp_home) ViewPager viewPager;
    TabLayout tabLayout;

    private List<BaseFragment> fragments;

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }


    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initView(View view) {
        List<String> titles = new ArrayList<>();
        titles.add("每日合集");
        titles.add(Category.android);
        titles.add(Category.iOS);
        titles.add(Category.frontEnd);
        titles.add(Category.resource);
        titles.add(Category.app);
        titles.add(Category.recommend);
        titles.add("妹子图");
        fragments = new ArrayList<>();
        for(String s : titles){
            switch (s) {
                case "每日合集":
                    fragments.add(DayFragment.newInstance());
                    break;
                case "妹子图":
                    fragments.add(WelfareFragment.newInstance(Category.welfare,false));
                    break;
                default:
                    fragments.add(CategoryFragment.newInstance(s));
                    break;
            }
        }
        TabAdapter tabAdapter = new TabAdapter(titles,fragments,getChildFragmentManager());
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs_home);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onRefresh() {
        int position = viewPager.getCurrentItem();
        if(fragments!= null && position < fragments.size()){
            fragments.get(position).onRefresh();
        }
    }
}
