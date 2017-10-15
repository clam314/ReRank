package com.clam314.rxrank.fragment.zhihu;

import android.support.design.widget.TabLayout;
import android.view.View;


import com.clam314.rxrank.fragment.BaseFragment;
import com.clam314.rxrank.fragment.BaseHomeFragment;
import com.clam314.rxrank.fragment.CategoryFragment;
import com.clam314.rxrank.fragment.DayFragment;
import com.clam314.rxrank.fragment.WelfareFragment;
import com.clam314.rxrank.http.Category;

import java.util.List;



public class ZhiHuHomeFragment extends BaseHomeFragment {

    public ZhiHuHomeFragment() {
    }


    public static ZhiHuHomeFragment newInstance() {
        return new ZhiHuHomeFragment();
    }

    @Override
    protected void initFragmentList(View view,List<String> titles, List<BaseFragment> fragments){
        titles.add("每日合集");
        titles.add("妹子图");
        for(String s : titles){
            switch (s) {
                case "每日合集":
                    fragments.add(DailyFragment.newInstance());
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
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
