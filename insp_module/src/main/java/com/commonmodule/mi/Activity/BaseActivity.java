package com.commonmodule.mi.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.commonmodule.mi.R;

import java.util.List;

/**
 * /**
 * Activity for Base methods of fragments
 * <p>
 * This activity is used to display different fragments with in activity
 * <p>
 * <p>
 * Created by Kundan on 27-09-2017.
 * Copyright (c) 2014 Kundan. All rights reserved.
 */
public class BaseActivity extends AppCompatActivity {

    public static final int FRAGMENT_JUST_REPLACE = 0;
    public static final int FRAGMENT_JUST_ADD = 1;
    public static final int FRAGMENT_ADD_TO_BACKSTACK_AND_REPLACE = 2;
    public static final int FRAGMENT_ADD_TO_BACKSTACK_AND_ADD = 3;

    public void addFragment(final Fragment fragment, final boolean isAddToBackStack, final boolean shouldAnimate) {
        pushFragment(fragment, isAddToBackStack, true, shouldAnimate, false);
    }

    public void replaceFragment(final Fragment fragment, final boolean isAddToBackStack, final boolean shouldAnimate) {
        pushFragment(fragment, isAddToBackStack, false, shouldAnimate, false);
    }

    public void addFragmentIgnorIfCurrent(final Fragment fragment, final boolean isAddToBackStack, final boolean shouldAnimate) {
        pushFragment(fragment, isAddToBackStack, true, shouldAnimate, true);
    }

    public void replaceFragmentIgnorIfCurrent(final Fragment fragment, final boolean isAddToBackStack, final boolean shouldAnimate) {
        pushFragment(fragment, isAddToBackStack, false, shouldAnimate, true);
    }

    private void pushFragment(final Fragment fragment, boolean isAddToBackStack, boolean isJustAdd, final boolean shouldAnimate, final boolean ignorIfCurrent) {
        if (fragment == null)
            return;
        // Add the fragment to the 'fragment_container' FrameLayout
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Find current visible fragment
        Fragment fragmentCurrent = fragmentManager.findFragmentById(R.id.fragment_container);

        if (ignorIfCurrent) {
            if (fragment.getClass().getCanonicalName().equalsIgnoreCase(fragmentCurrent.getTag())) {
                return;
            }
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (shouldAnimate) {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        } else {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }

        if (fragmentCurrent != null) {
            fragmentTransaction.hide(fragmentCurrent);
        }

        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getCanonicalName());
        }

        if (isJustAdd) {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName());
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName());
        }

        fragmentTransaction.commit();

    }

    public void pushFragmentIgnoreCurrent(Fragment fragment, int fragmentTransactionType) {
        switch (fragmentTransactionType) {
            case FRAGMENT_JUST_REPLACE:
                pushFragments(R.id.fragment_container, null, fragment, false, false, true, false);
                break;
            case FRAGMENT_JUST_ADD:
                pushFragments(R.id.fragment_container, null, fragment, true, false, true, true);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_REPLACE:
                pushFragments(R.id.fragment_container, null, fragment, true, true, true, false);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_ADD:
                pushFragments(R.id.fragment_container, null, fragment, true, true, true, true);
                break;
        }
    }

    public void pushFragmentDontIgnoreCurrent(Fragment fragment, int fragmentTransactionType) {
        switch (fragmentTransactionType) {
            case FRAGMENT_JUST_REPLACE:
                pushFragments(R.id.fragment_container, null, fragment, true, false, false, false);
                break;
            case FRAGMENT_JUST_ADD:
                pushFragments(R.id.fragment_container, null, fragment, true, false, false, true);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_REPLACE:
                pushFragments(R.id.fragment_container, null, fragment, true, true, false, false);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_ADD:
                pushFragments(R.id.fragment_container, null, fragment, true, true, false, true);
                break;
        }
    }

    public void pushFragments(final int id, final Bundle args,
                              final Fragment fragment, boolean shouldAnimate,
                              final boolean shouldAdd, final boolean ignoreIfCurrent, final boolean justAdd) {

        pushFragments(id, args, fragment, null, shouldAnimate, shouldAdd, ignoreIfCurrent, justAdd);
    }

    public void pushFragments(final int id, final Bundle args,
                              final Fragment fragment, final Fragment fragmentParent, boolean shouldAnimate,
                              final boolean shouldAdd, final boolean ignoreIfCurrent, final boolean justAdd) {

        try {

            int i = 0;
            hideKeyboard();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (ignoreIfCurrent && currentFragment != null && fragment != null && currentFragment.getClass().equals(fragment.getClass())) {
                return;
            }

            // assert fragment != null;
            if (fragment.getArguments() == null) {
                fragment.setArguments(args);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (shouldAdd) {
                //  ft.addToBackStack(null);
                ft.addToBackStack(fragment.getClass().getCanonicalName() + (i + 1));
                //ft.add(id, fragment, fragment.getClass().getCanonicalName()+(i+1));
            }

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            if (justAdd) {
                removeIfExists(fragmentManager, fragment);

                //ft.add(id, fragment, fragment.getClass().getCanonicalName()+(i+1));
                ft.add(id, fragment, fragment.getClass().getCanonicalName() + (i + 1));

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
                    // Fragment fragmentNew = context.getSupportFragmentManager().findFragmentById(R.id.container);
                    ft.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
                }

            } else {
                ft.replace(R.id.fragment_container, fragment);
            }

            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeIfExists(FragmentManager fragmentManager, Fragment fragment) {
        Fragment f = fragmentManager.findFragmentByTag(fragment.getClass().getCanonicalName());
        if (f != null && f.getClass().equals(fragment.getClass())) {
            fragmentManager.beginTransaction().remove(f).commit();
            fragmentManager.popBackStack();
        }
    }


    public Fragment getVisibleFragment() {
        try {
            //fsdjfskskf
            FragmentManager fragmentManager = getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            Fragment fragment1;
            if (fragments != null) {
                for (Fragment fragment : fragments) {

                    if (fragment != null && fragment.isVisible())

                        return fragment;


                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }

    public void clearBackStack() {

        FragmentManager fm = getSupportFragmentManager();

        int count = fm.getBackStackEntryCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                fm.popBackStack();
            }
        }

    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void hideKeyboard() {
        try {
            View view = getCurrentFocus();
            if (view != null) {
                view.clearFocus();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popBackStack() {

        try {
            FragmentManager fm = getSupportFragmentManager();
            for (Fragment frag : fm.getFragments()) {
                if (frag != null && frag.isVisible()) {
                    FragmentManager childFm = frag.getChildFragmentManager();

                    if (childFm != null && childFm.getBackStackEntryCount() > 0) {
                        childFm.popBackStack();
                        return;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
