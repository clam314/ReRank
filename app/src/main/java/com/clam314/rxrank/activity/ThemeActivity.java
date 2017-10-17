package com.clam314.rxrank.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.clam314.rxrank.R;
import com.clam314.rxrank.entity.zhihu.Section;
import com.clam314.rxrank.entity.zhihu.Theme;
import com.clam314.rxrank.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ThemeActivity extends BaseActivity {
    public static final int TYPE_THEME = 0;
    public static final int TYPE_SECTION = 1;

    private static final String KEY_TYPE = "KEY_TYPE";
    private static final String KEY_DATA = "KEY_DATA";

    @BindView(R.id.rv_theme)
    RecyclerView recyclerView;
    @BindView(R.id.tb_title)
    Toolbar toolbar;


    private Theme mTheme;
    private Section mSection;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra(KEY_TYPE, 0);
        if (mType == TYPE_THEME) {
          mTheme = getIntent().getParcelableExtra(KEY_DATA);
        } else {
            
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_toobar_web,menu);
//        return true;
//    }

    private void initToolBar(String title) {
        toolbar.setTitle(StringUtil.getShowStringNotNull(title));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
