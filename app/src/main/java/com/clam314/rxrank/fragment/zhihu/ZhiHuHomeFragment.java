package com.clam314.rxrank.fragment.zhihu;

import android.support.design.widget.TabLayout;
import android.view.View;


import com.clam314.rxrank.entity.zhihu.Section;
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
    protected void initFragmentList(View view, List<String> titles, List<BaseFragment> fragments) {
        titles.add("每日");
        titles.add("主题");
        titles.add("栏目");
        for (String s : titles) {
            switch (s) {
                case "每日":
                    fragments.add(DailyFragment.newInstance());
                    break;
                case "主题":
                    fragments.add(ThemeFragment.newInstance());
                    break;
                case "栏目":
                    fragments.add(SectionFragment.newInstance());
                    break;
            }
        }
    }

    @Override
    public void onChangeTabLayoutAfter(TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
