package com.clam314.rxrank.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.clam314.rxrank.R;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.util.DeBugLog;
import com.clam314.rxrank.util.StringUtil;
import com.clam314.rxrank.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {
    private static final String TAG = WebActivity.class.getSimpleName();
    public static final String PARAM_ITEM = "item";

    private Item mItem;

    @BindView(R.id.tb_web) Toolbar toolbar;
    @BindView(R.id.pb_web) ProgressBar progressBar;
    @BindView(R.id.wv_web) WebView wbContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        mItem = getIntent().getParcelableExtra(PARAM_ITEM);
        if(mItem !=null){
            initToolBar(mItem);
            initWebView(mItem);
        }else {
            Toast.makeText(this,"数据异常",Toast.LENGTH_SHORT).show();
            DeBugLog.logError(TAG,"item为空");
            finish();
        }
        ViewUtil.statusBarCompat(this,toolbar);
    }

    private void initToolBar(Item item){
        toolbar.setTitle(StringUtil.getShowStringNotNull(item.getDesc()));
        setSupportActionBar(toolbar);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(Item item){
        wbContent.loadUrl(item.getUrl());
        wbContent.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(Build.VERSION.SDK_INT >= 21){
                    shouldOverrideUrlLoading(view,request.getUrl().toString());
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        wbContent.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress >= 100){
                    progressBar.setVisibility(View.GONE);
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
        wbContent.getSettings().setAllowFileAccess(true);
        wbContent.getSettings().setJavaScriptEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toobar_web,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,mItem.getUrl());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent,"分享到..."));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
