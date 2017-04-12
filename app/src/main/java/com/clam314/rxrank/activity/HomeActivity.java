package com.clam314.rxrank.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.clam314.rxrank.R;
import com.clam314.rxrank.fragment.BaseFragment;
import com.clam314.rxrank.fragment.HomeFragment;
import com.clam314.rxrank.fragment.SettingFragment;
import com.clam314.rxrank.fragment.WelfareFragment;
import com.clam314.rxrank.http.Category;
import com.clam314.rxrank.util.DeBugLog;
import com.clam314.rxrank.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final String SAVE_CURRENT_ID = "current_id";
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

    private SparseArray<Fragment> drawerFragments;

    private FragmentManager fragmentManager;
    private int currentFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        currentFragmentId = 0;
        initView();
        initFragments(savedInstanceState);
        ViewUtil.statusBarCompat(this,clHome);
        DeBugLog.logError("home","onCreate");
    }

    private void initFragments(Bundle savedInstanceState){
        if(drawerFragments == null){
            drawerFragments = new SparseArray<>();
            drawerFragments.put(MENU_ID[0], HomeFragment.newInstance());
            drawerFragments.put(MENU_ID[1],null);
            drawerFragments.put(MENU_ID[2], WelfareFragment.newInstance(Category.welfare,true));
            drawerFragments.put(MENU_ID[3], SettingFragment.newInstance());
            drawerFragments.put(MENU_ID[4],null);
        }

        if(savedInstanceState == null){
            currentFragmentId = MENU_ID[0];
        }else {
            int id = savedInstanceState.getInt(SAVE_CURRENT_ID,MENU_ID[0]);
            changeTabLayoutStatus("设置",id);
            currentFragmentId = id;
        }
        fragmentManager.beginTransaction().replace(R.id.fragment_main, drawerFragments.get(currentFragmentId)).commit();
    }


    private void initView(){
        tbHome.setTitle("首页");
        setSupportActionBar(tbHome);
        dlHome.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ActionBarDrawerToggle mDrawerToggle = new DrawerListener(this, dlHome, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        dlHome.addDrawerListener(mDrawerToggle);
        nvHome.setNavigationItemSelectedListener(this);
        //清除小图标的颜色渲染，显示图标本身的颜色
        nvHome.setItemIconTintList(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_CURRENT_ID,currentFragmentId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        changeRefreshIconVisibility(currentFragmentId);
        DeBugLog.logError("home","onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                dlHome.openDrawer(GravityCompat.START);
                return true;
            case R.id.refresh:
                if(drawerFragments.get(currentFragmentId) instanceof BaseFragment){
                    ((BaseFragment)drawerFragments.get(currentFragmentId)).onRefresh();
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
            changeTabLayoutStatus(item.getTitle(),item.getItemId());
            changeRefreshIconVisibility(item.getItemId());
            changeContentFragment(drawerFragments.get(currentFragmentId),drawerFragments.get(item.getItemId()));
            currentFragmentId = item.getItemId();
        }
        dlHome.closeDrawers();
        return true;
    }

    private void changeTabLayoutStatus(CharSequence title, int itemId){
        tbHome.setTitle(title);
        if(currentFragmentId == MENU_ID[0]){
            tabHome.setVisibility(View.GONE);
        }else if(itemId == MENU_ID[0]){
            tabHome.setVisibility(View.VISIBLE);
        }else {
            tabHome.setVisibility(View.GONE);
        }
    }

    private void changeRefreshIconVisibility(int itemId){
        tbHome.getMenu().findItem(R.id.refresh).setVisible(itemId != MENU_ID[3]);
    }

    private void changeContentFragment(Fragment from, Fragment to){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(!to.isAdded()){
            transaction.hide(from).add(R.id.fragment_main,to).commit();
        }else {
            transaction.hide(from).show(to).commit();
        }
    }


    private class DrawerListener extends ActionBarDrawerToggle{

        DrawerListener(Activity activity, DrawerLayout drawerLayout, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            if(drawerView instanceof NavigationView){
                View view = ((NavigationView) drawerView).getHeaderView(0).findViewById(R.id.iv_item);
                view.setRotation(360*slideOffset);
            }

        }

    }

}
