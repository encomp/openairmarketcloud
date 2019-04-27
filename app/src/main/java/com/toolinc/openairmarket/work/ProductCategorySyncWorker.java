package com.toolinc.openairmarket.work;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.inject.assisted.Assisted;
import com.squareup.inject.assisted.AssistedInject;
import com.toolinc.openairmarket.common.work.ListenableWorkerFactory;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Categories;
import com.toolinc.openairmarket.persistence.sync.DataSync;

import java.util.concurrent.ExecutionException;

import timber.log.Timber;

/** Downloads the product category information from firestore database. */
public class ProductCategorySyncWorker extends Worker {

  private static final String TAG = ProductCategorySyncWorker.class.getSimpleName();

  private final Application application;
  private final DataSync dataSync;

  @AssistedInject
  public ProductCategorySyncWorker(
      Application application,
      @Categories DataSync dataSync,
      @Assisted Context context,
      @Assisted WorkerParameters workerParameters) {
    super(context, workerParameters);
    this.application = application;
    this.dataSync = dataSync;
  }

  @NonNull
  @Override
  public Result doWork() {
    Task<QuerySnapshot> task = dataSync.refresh(application.getApplicationContext());
    Timber.tag(TAG).d("Waiting for the task to complete...");
    try {
      Tasks.await(task);
      if (task.isSuccessful()) {
        return Result.success();
      }
    } catch (ExecutionException | InterruptedException exc) {
      Timber.tag(TAG).e(exc.getMessage());
    }
    return Result.failure();
  }

  @AssistedInject.Factory
  /**
   * Marker interface to support dependency injection for {@link ProductCategorySyncWorker}
   * instances.
   */
  public interface Factory extends ListenableWorkerFactory {}
}
