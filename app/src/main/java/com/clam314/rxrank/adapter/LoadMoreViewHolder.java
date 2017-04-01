package com.clam314.rxrank.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.clam314.rxrank.R;

/**
 * Created by clam314 on 2017/4/1
 */

public class LoadMoreViewHolder extends LoadMoreWrapperAdapter.LoadStatusViewHolder {

    private LoadMoreViewHolder(View view) {
        super(view);
    }

    public static LoadMoreViewHolder newInstance(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.holder_load_more,null,false);
        //因为inflater时传了parent为null，match_parent的属性失效了,在这里设置一遍
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //添加一个旋转动画
        final ObjectAnimator animator = ObjectAnimator.ofFloat(view.findViewById(R.id.iv_load),"rotation",0f,360f);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                if(!animator.isRunning())animator.start();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                //当view离开屏幕后，停止动画，防止内存泄漏
                if(animator.isRunning())animator.cancel();
            }
        });
        return new LoadMoreViewHolder(view);
    }
}
