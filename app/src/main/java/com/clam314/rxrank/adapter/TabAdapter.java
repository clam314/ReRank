package com.clam314.rxrank.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.clam314.rxrank.fragment.BaseFragment;

import java.util.List;

/**
 * Created by clam314 on 2016/3/1
 */
public class TabAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> list;
    private List<String> tabTitles;

    public TabAdapter(List<String> tabTitles, List<BaseFragment> list, FragmentManager fragmentManager){
        super(fragmentManager);
        this.list = list;
        this.tabTitles = tabTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(tabTitles != null && tabTitles.size() - 1 >= position){
            return tabTitles.get(position);
        }else {
            return "UnKnow";
        }
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
