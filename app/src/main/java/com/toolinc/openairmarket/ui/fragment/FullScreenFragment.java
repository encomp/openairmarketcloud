package com.toolinc.openairmarket.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.toolinc.openairmarket.R;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

/** Base Fragment class that provides a full screen structure for a view. */
public abstract class FullScreenFragment extends Fragment implements HasSupportFragmentInjector {

  private static final String TAG = FullScreenFragment.class.getSimpleName();
  public static final String SCREEN_TITLE = "screen_title";

  private Toolbar mToolbar;
  private ViewGroup mViewGroup;
  @Inject DispatchingAndroidInjector<Fragment> childFragmentInjector;

  @Override
  public void onAttach(Context context) {
    safeInject();
    super.onAttach(context);
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view =
        layoutInflater.inflate(R.layout.fragment_fullscreen, viewGroup, false /* attachToRoot */);
    mToolbar = view.findViewById(R.id.toolbar);
    mViewGroup = view.findViewById(R.id.full_screen_fragment_container);
    initDemoActionBar();
    mViewGroup.addView(onCreateFullScreenView(layoutInflater, viewGroup, bundle));
    return view;
  }

  public abstract View onCreateFullScreenView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle);

  private void initDemoActionBar() {
    if (shouldShowDefaultActionBar()) {
      AppCompatActivity activity = (AppCompatActivity) getActivity();
      activity.setSupportActionBar(mToolbar);
      setActionBarTitle(activity.getSupportActionBar());
    } else {
      mToolbar.setVisibility(View.GONE);
    }
  }

  private void setActionBarTitle(ActionBar actionBar) {
    if (getTitleResId() != 0) {
      actionBar.setTitle(getTitleResId());
    } else {
      actionBar.setTitle(getDefaultTitle());
    }
  }

  protected String getDefaultTitle() {
    Bundle args = getArguments();
    if (args != null) {
      return args.getString(SCREEN_TITLE, "");
    } else {
      return "";
    }
  }

  @StringRes
  public int getTitleResId() {
    return 0;
  }

  /**
   * Whether this fragment wants to use the default demo action bar, or if the fragment wants to use
   * its own Toolbar as the action bar.
   */
  public boolean shouldShowDefaultActionBar() {
    return true;
  }

  @Override
  public AndroidInjector<Fragment> supportFragmentInjector() {
    return childFragmentInjector;
  }

  private void safeInject() {
    try {
      AndroidSupportInjection.inject(this);
    } catch (Exception exc) {
      Timber.tag(TAG).e(exc);
    }
  }
}
