package com.toolinc.openairmarket.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.fragment.MainFragment;
import com.toolinc.openairmarket.ui.fragment.OnBackPressedHandler;
import com.toolinc.openairmarket.viewmodel.ReceiptsViewModel;
import com.toolinc.openairmarket.work.ProductCategorySyncWorker;
import com.toolinc.openairmarket.work.ProductSyncWorker;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/** Main activity. */
public final class MainActivity extends DaggerAppCompatActivity {

  @Inject ViewModelProvider.Factory viewModelFactory;
  private ReceiptsViewModel receiptsViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    receiptsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ReceiptsViewModel.class);

    if (savedInstanceState == null) {
      MainFragment mainFragment = new MainFragment();
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
