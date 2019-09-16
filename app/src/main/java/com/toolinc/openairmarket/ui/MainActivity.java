package com.toolinc.openairmarket.ui;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.toolinc.openairmarket.R;
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
    syncFromFirestore();
  }

  private void syncFromFirestore() {
    WorkManager.getInstance(getApplicationContext())
        .enqueue(
            new OneTimeWorkRequest.Builder(ProductCategorySyncWorker.class)
                .setInitialDelay(2000, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS)
                .build());

    WorkManager.getInstance(getApplicationContext())
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
