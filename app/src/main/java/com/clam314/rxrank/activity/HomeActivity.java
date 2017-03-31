package com.clam314.rxrank.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.clam314.rxrank.R;
import com.clam314.rxrank.fragment.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.tb_home) Toolbar tbHome;
    @BindView(R.id.dl_home) DrawerLayout dlHome;
    @BindView(R.id.nv_home) NavigationView nvHome;


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        initView();
    }

    private void initView(){
        setSupportActionBar(tbHome);

        dlHome.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, dlHome, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        dlHome.addDrawerListener(mDrawerToggle);
        nvHome.setNavigationItemSelectedListener(this);
        //清除小图标的颜色渲染，显示图标本身的颜色
        nvHome.setItemIconTintList(null);
        fragmentManager.beginTransaction().replace(R.id.fragment_main, HomeFragment.newInstance()).commit();
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
        item.setChecked(true);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.homepage:


                break;
            case R.id.been:
//                BeenFragment beenFragment = new BeenFragment("福利");
//                MainFragment mainFragment1 = (MainFragment)fragmentManager.findFragmentById(R.id.fragment_main);
//                transaction.replace(R.id.fragment_main, beenFragment);
//                transaction.commit();
                break;
        }
        dlHome.closeDrawers();
        return true;
    }
}
