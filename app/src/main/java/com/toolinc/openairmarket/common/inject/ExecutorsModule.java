package com.toolinc.openairmarket.common.inject;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    return Executors.newFixedThreadPool(3);
  }

  @Global.MainThread
  @Singleton
  @Provides
  static Executor providesMainThreadExecutor() {
    return new MainThreadExecutor();
  }

  private static class MainThreadExecutor implements Executor {
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable command) {
      mainThreadHandler.post(command);
    }
  }
}
