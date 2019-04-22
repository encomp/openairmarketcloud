package com.toolinc.openairmarket.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.inject.assisted.Assisted;
import com.squareup.inject.assisted.AssistedInject;
import com.toolinc.openairmarket.common.work.ListenableWorkerFactory;

/** Downloads the product information from firestore database. */
public class ProductWorker extends Worker {

  private FirebaseFirestore firebaseFirestore;

  @AssistedInject
  public ProductWorker(
      FirebaseFirestore firebaseFirestore,
      @Assisted Context context,
      @Assisted WorkerParameters workerParameters) {
    super(context, workerParameters);
    this.firebaseFirestore = firebaseFirestore;
  }

  @NonNull
  @Override
  public Result doWork() {
    return Result.success();
  }

  @AssistedInject.Factory
  /** Marker interface to support dependency injection for {@link ProductWorker} instances. */
  public interface Factory extends ListenableWorkerFactory {}
}
