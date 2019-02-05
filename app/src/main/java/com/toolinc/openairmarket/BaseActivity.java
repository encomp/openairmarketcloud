package com.toolinc.openairmarket;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

  @VisibleForTesting public ProgressBar mProgressDialog;

  public void showProgressDialog() {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressBar(this);
      mProgressDialog.setIndeterminate(true);
    }

    mProgressDialog.setVisibility(View.VISIBLE);
  }

  public void hideProgressDialog() {
    if (mProgressDialog != null) {
      mProgressDialog.setVisibility(View.INVISIBLE);
    }
  }

  public void hideKeyboard(View view) {
    final InputMethodManager imm =
        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    hideProgressDialog();
  }
}
