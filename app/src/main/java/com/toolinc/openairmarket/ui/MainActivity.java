package com.toolinc.openairmarket.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.fragment.MainFragment;
import com.toolinc.openairmarket.ui.fragment.OnBackPressedHandler;
import com.toolinc.openairmarket.work.ProductCategorySyncWorker;
import com.toolinc.openairmarket.work.ProductSyncWorker;

import java.util.concurrent.TimeUnit;

import dagger.android.support.DaggerAppCompatActivity;

/** Main activity. */
public final class MainActivity extends DaggerAppCompatActivity {

  MainFragment mainFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
      mainFragment = new MainFragment();
      getSupportFragmentManager().beginTransaction().add(R.id.container, mainFragment).commit();
    }
    syncFromFirestore();
  }

  @Override
  public void onBackPressed() {
    if (handleFragmentOnBackPressed()) {
      return;
    }
    super.onBackPressed();
  }

  private boolean handleFragmentOnBackPressed() {
    Fragment currentFragment = FragmentUtils.getCurrentFragment(this);
    return currentFragment instanceof OnBackPressedHandler
        && ((OnBackPressedHandler) currentFragment).onBackPressed();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void syncFromFirestore() {
    WorkManager.getInstance()
        .enqueue(
            new OneTimeWorkRequest.Builder(ProductCategorySyncWorker.class)
                .setInitialDelay(2000, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS)
                .build());

    WorkManager.getInstance()
        .enqueue(
            new OneTimeWorkRequest.Builder(ProductSyncWorker.class)
                .setInitialDelay(2000, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS)
                .build());
  }
}
