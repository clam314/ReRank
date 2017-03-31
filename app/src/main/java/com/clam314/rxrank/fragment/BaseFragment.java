package com.clam314.rxrank.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;

    public BaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView(inflater,container,savedInstanceState);
        unbinder = ButterKnife.bind(this,view);
        initView(view);
        doAfterInitView(view);
        return view;
    }

    protected abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    protected abstract  void initView(View view);
    protected void doAfterInitView(View view){}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
