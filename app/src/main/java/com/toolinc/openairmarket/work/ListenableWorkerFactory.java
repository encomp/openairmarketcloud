package com.toolinc.openairmarket.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

/** Factory interface to allow the creation of {@link ListenableWorker} using dagger. */
public interface ListenableWorkerFactory {

  /** Factory method to create a new {@link ListenableWorker} to execute a background task. */
  @NonNull
  ListenableWorker create(@NonNull Context context, @NonNull WorkerParameters workerParameters);
}
