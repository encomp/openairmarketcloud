package com.toolinc.openairmarket.common.work.inject;

import com.toolinc.openairmarket.common.work.WorkerFactoryDagger;

import dagger.Module;

/** Defines the workers for DI. */
@Module
public interface WorkerFactoryDaggerModule {

  /** Provides a {@link androidx.work.WorkerFactory} instance that support dependency injection. */
  WorkerFactoryDagger workerFactory();
}
