package com.pachia.comon.game;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by dungnv
 */
public abstract class BaseFragment extends Fragment {
    protected boolean isRefresh = false;
    protected Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResource(), container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mActivity = activity;

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * @param frameId
     * @param fragment
     * @param addToBackStack
     * @param tag
     */

    public void addFragment(int frameId, Fragment fragment, boolean addToBackStack, String tag) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Fragment oldFrag = getChildFragmentManager().findFragmentByTag(tag);
        if (oldFrag != null)
            getChildFragmentManager().beginTransaction().remove(oldFrag).commit();
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.add(frameId, fragment, tag);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public void replaceFragment(int frameId, Fragment fragment, boolean addToBackStack, String tag) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.replace(frameId, fragment, tag);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }


    /**
     * base startActivity from fragment
     *
     * @param c
     */
    public void startActivity(Class<?> c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }

    /**
     * Layout of activity
     *
     * @return id of resource layout
     */
    @LayoutRes
    protected abstract int getLayoutResource();


}
