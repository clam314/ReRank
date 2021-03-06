package com.clam314.rxrank.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.clam314.rxrank.MainApplication;
import com.clam314.rxrank.R;
import com.clam314.rxrank.entity.ImageCache;
import com.clam314.rxrank.presenter.DataPresenter;
import com.clam314.rxrank.util.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {
    @BindView(R.id.sv_main) SimpleDraweeView svImage;
    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            Transition fade = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_top);
            getWindow().setExitTransition(fade);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }


    private void initView(){
        MainApplication.getInstance().getPresenter(DataPresenter.class).loadHomeImage(getBaseContext(),new Observer<ImageCache>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ImageCache cache) {
                if(!TextUtils.isEmpty(cache.getSavePath())){
                    FrescoUtil.loadImage(Uri.parse("file://"+ cache.getSavePath()),svImage,null,0,0,null);
                }else {
                    svImage.setImageURI((new Uri.Builder().scheme("res").path(String.valueOf(R.drawable.image_default))).build());
                }
            }

            @Override
            public void onError(Throwable e) {
                svImage.setImageURI((new Uri.Builder().scheme("res").path(String.valueOf(R.drawable.image_default))).build());
                startAnimator(svImage);
            }

            @Override
            public void onComplete() {
                startAnimator(svImage);
            }
        });
    }

    private void startAnimator(View view){
        PropertyValuesHolder xHolder = PropertyValuesHolder.ofFloat("scaleX",1,1.1f);
        PropertyValuesHolder yHolder = PropertyValuesHolder.ofFloat("scaleY",1,1.1f);
        animator = ObjectAnimator.ofPropertyValuesHolder(view,xHolder,yHolder);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(3000);
        animator.setRepeatCount(0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent a = new Intent(MainActivity.this,HomeActivity.class);
                if(Build.VERSION.SDK_INT >= 21){
                    startActivity(a, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                    finishAfterTransition();
                }else {
                    startActivity(a);
                    finish();
                }
            }
        });
        animator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(animator!=null && animator.isRunning()) animator.cancel();
    }
}
