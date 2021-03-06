package com.clam314.rxrank.fragment;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.clam314.rxrank.http.Category;

import java.util.List;


public class HomeFragment extends BaseHomeFragment {

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }


    @Override
    protected void initFragmentList(View view,List<String> titles, List<BaseFragment> fragments){
        titles.add("每日合集");
        titles.add(Category.android);
        titles.add(Category.iOS);
        titles.add(Category.frontEnd);
        titles.add(Category.resource);
        titles.add(Category.app);
        titles.add(Category.recommend);
        titles.add("妹子图");
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
    }

    @Override
    public void onChangeTabLayoutAfter(TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
}
