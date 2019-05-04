package com.toolinc.openairmarket.common.inject;

import com.toolinc.openairmarket.common.concurrent.MainThreadExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/** Provides the executors for the app. */
@Module
public class ExecutorsModule {

  @Global.DiskIO
  @Singleton
  @Provides
  static Executor providesDiskIOExecutor() {
    return Executors.newSingleThreadExecutor();
  }

  @Global.NetworkIO
  @Singleton
  @Provides
  static Executor providesNetworkIOExecutor() {
    int numCores = Runtime.getRuntime().availableProcessors();
    return new ThreadPoolExecutor(
        numCores * 2, numCores * 2, 120L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
  }

  @Singleton
  @Provides
  static MainThreadExecutor providesMainThreadExecutor() {
    return new MainThreadExecutor();
  }
}
