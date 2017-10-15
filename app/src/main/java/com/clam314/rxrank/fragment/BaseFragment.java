package com.clam314.rxrank.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam314.rxrank.util.DeBugLog;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {
    private String TAG = getClass().getSimpleName();
    private Unbinder unbinder;
    private boolean isFirst;
    private boolean isOk;
    private View rootView;
    private Bundle mSave;

    public BaseFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        DeBugLog.logInfo(TAG,"setUserVisibleHint-"+isFirst+"-"+isVisibleToUser);
        if(!isFirst && isVisibleToUser){
            isFirst = true;
            if(rootView != null) doAfterInitView(rootView,mSave);
            isOk = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSave = savedInstanceState;
        rootView = createView(inflater,container,savedInstanceState);
        unbinder = ButterKnife.bind(this,rootView);
        initView(rootView);
        DeBugLog.logInfo(TAG,"onCreateView-"+isFirst);
        if(isOk) doAfterInitView(rootView,savedInstanceState);
        return rootView;
    }

    protected abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    protected abstract  void initView(View view);

    protected void doAfterInitView(View view, @Nullable Bundle savedInstanceState){}
    public void onRefresh(){}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeBugLog.logInfo(TAG,"onCreate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        DeBugLog.logInfo(TAG,"onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        DeBugLog.logInfo(TAG,"onStart");
    }


    @Override
    public void onResume() {
        super.onResume();
        DeBugLog.logInfo(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        DeBugLog.logInfo(TAG,"onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        DeBugLog.logInfo(TAG,"onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        rootView = null;
        mSave = null;
        DeBugLog.logInfo(TAG,"onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        DeBugLog.logInfo(TAG,"onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DeBugLog.logInfo(TAG,"onDestroy");
    }
}
