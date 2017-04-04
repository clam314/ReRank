package com.clam314.rxrank.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clam314.rxrank.R;
import com.clam314.rxrank.fragment.BaseFragment;
import com.clam314.rxrank.fragment.HomeFragment;
import com.clam314.rxrank.fragment.WelfareFragment;
import com.clam314.rxrank.http.Category;
import com.clam314.rxrank.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.tb_home) Toolbar tbHome;
    @BindView(R.id.dl_home) DrawerLayout dlHome;
    @BindView(R.id.nv_home) NavigationView nvHome;
    @BindView(R.id.tabs_home) TabLayout tabHome;
    @BindView(R.id.cl_home) CoordinatorLayout clHome;

    private static final int[] MENU_ID = new int[]{
            R.id.menu_home,
            R.id.menu_collection,
            R.id.menu_welfare,
            R.id.menu_settings,
            R.id.menu_help
    };

    private SparseArray<BaseFragment> drawerFragments;

    private FragmentManager fragmentManager;
    private int currentFragmentId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        initView();
        initFragments();
        ViewUtil.statusBarCompat(this,clHome);
    }

    private void initFragments(){
        drawerFragments = new SparseArray<>();
        drawerFragments.put(MENU_ID[0], HomeFragment.newInstance());
        drawerFragments.put(MENU_ID[1],null);
        drawerFragments.put(MENU_ID[2], WelfareFragment.newInstance(Category.welfare,true));
        drawerFragments.put(MENU_ID[3],null);
        drawerFragments.put(MENU_ID[4],null);

        currentFragmentId = MENU_ID[0];
        fragmentManager.beginTransaction().replace(R.id.fragment_main, drawerFragments.get(currentFragmentId)).commit();
    }


    private void initView(){
        tbHome.setTitle("首页");
        setSupportActionBar(tbHome);
        dlHome.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, dlHome, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        dlHome.addDrawerListener(mDrawerToggle);
        nvHome.setNavigationItemSelectedListener(this);
        //清除小图标的颜色渲染，显示图标本身的颜色
        nvHome.setItemIconTintList(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                dlHome.openDrawer(GravityCompat.START);
                return true;
            case R.id.refresh:
                if(drawerFragments.get(currentFragmentId) != null){
                    drawerFragments.get(currentFragmentId).onRefresh();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //当点击返回键是，若抽屉打开则关闭
        if(dlHome.isDrawerOpen(GravityCompat.START)){
            dlHome.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //TODO 其他页面暂时没实现
        if(null == drawerFragments.get(item.getItemId())){
            Toast.makeText(this,"即将到来……",Toast.LENGTH_SHORT).show();
            dlHome.closeDrawers();
            item.setCheckable(false);
            return true;
        }

        item.setChecked(true);
        if(item.getItemId() != currentFragmentId){
            tbHome.setTitle(item.getTitle());
            if(currentFragmentId == MENU_ID[0]){
                tabHome.setVisibility(View.GONE);
            }else if(item.getItemId() == MENU_ID[0]){
                tabHome.setVisibility(View.VISIBLE);
            }
            changeContentFragment(drawerFragments.get(currentFragmentId),drawerFragments.get(item.getItemId()));
            currentFragmentId = item.getItemId();
        }
        dlHome.closeDrawers();
        return true;
    }

    private void changeContentFragment(BaseFragment from, BaseFragment to){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(!to.isAdded()){
            transaction.hide(from).add(R.id.fragment_main,to).commit();
        }else {
            transaction.hide(from).show(to).commit();
        }
    }
}
