package com.toolinc.openairmarket.common.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import javax.inject.Provider;

/** A factory object that creates {@link ListenableWorker} instances using Dagger. */
public class WorkerFactoryDagger extends WorkerFactory {

  final ImmutableMap<Class<ListenableWorker>, Provider<ListenableWorkerFactory>> factories;

  @Inject
  public WorkerFactoryDagger(
      ImmutableMap<Class<ListenableWorker>, Provider<ListenableWorkerFactory>> factories) {
    this.factories = factories;
  }

  @NonNull
  @Override
  public ListenableWorker createWorker(
      @NonNull Context appContext,
      @NonNull String workerClassName,
      @NonNull WorkerParameters workerParameters) {

    try {
      Class<?> clazz = Class.forName(workerClassName);
      return factories.get(clazz).get().create(appContext, workerParameters);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
          String.format("Unknown worker class name: [%s].", workerClassName));
    }
  }
}
