package com.clam314.rerank.fragment;

import android.net.Uri;
import android.support.v4.app.Fragment;


public class BaseFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public BaseFragment() {
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
