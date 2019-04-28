package com.toolinc.openairmarket.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.toolinc.openairmarket.R;

/** Utils for fragments. */
public abstract class FragmentUtils {

  private static final int MAIN_ACTIVITY_FRAGMENT_CONTAINER_ID = R.id.container;

  public static void startFragment(FragmentActivity activity, Fragment fragment, String tag) {
    activity
        .getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(
            com.google.android.material.R.anim.abc_grow_fade_in_from_bottom,
            com.google.android.material.R.anim.abc_fade_out,
            com.google.android.material.R.anim.abc_fade_in,
            com.google.android.material.R.anim.abc_shrink_fade_out_from_bottom)
        .replace(MAIN_ACTIVITY_FRAGMENT_CONTAINER_ID, fragment, tag)
        .addToBackStack(null /* name */)
        .commit();
  }

  public static Fragment getCurrentFragment(FragmentActivity activity) {
    return activity
        .getSupportFragmentManager()
        .findFragmentById(MAIN_ACTIVITY_FRAGMENT_CONTAINER_ID);
  }
}
