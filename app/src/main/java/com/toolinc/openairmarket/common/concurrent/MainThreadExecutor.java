package com.toolinc.openairmarket.common.concurrent;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * An object that executes submitted {@link Runnable} tasks on the UI thread.
 */
public class MainThreadExecutor implements Executor {
  private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

  @Override
  public void execute(@NonNull Runnable command) {
    mainThreadHandler.post(command);
  }
}
