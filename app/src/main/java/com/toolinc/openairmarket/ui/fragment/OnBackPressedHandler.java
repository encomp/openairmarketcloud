package com.toolinc.openairmarket.ui.fragment;

/** Interface that allows Catalog Fragments to handle the main Activity's onBackPressed() */
public interface OnBackPressedHandler {

  /** Take care of popping the fragment back stack or finishing the activity as appropriate. */
  boolean onBackPressed();
}
