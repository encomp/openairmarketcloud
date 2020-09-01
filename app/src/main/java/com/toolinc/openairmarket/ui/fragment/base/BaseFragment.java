package com.toolinc.openairmarket.ui.fragment.base;

import androidx.fragment.app.Fragment;

import com.toolinc.openairmarket.R;

/** Base fragment. */
public class BaseFragment extends Fragment {

  protected boolean isLandscape() {
    return getActivity().findViewById(R.id.top_app_bar) != null;
  }

  protected boolean isPortrait() {
    return getActivity().findViewById(R.id.bottom_app_bar) != null;
  }
}
