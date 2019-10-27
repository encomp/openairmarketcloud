package com.toolinc.openairmarket.common.lifecycle;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/** Utility class that facilitates the {@link LiveData} testing. */
public final class LiveDataTestUtil {

  private LiveDataTestUtil() {}

  /**
   * Get the value from a LiveData object. We're waiting for LiveData to emit, for 2 seconds. Once
   * we got a notification via onChanged, we stop observing.
   */
  public static <T> T blocking(final LiveData<T> liveData) throws InterruptedException {
    final Object[] data = new Object[1];
    final CountDownLatch latch = new CountDownLatch(1);
    Observer<T> observer =
        new Observer<T>() {
          @Override
          public void onChanged(@Nullable T o) {
            data[0] = o;
            latch.countDown();
            liveData.removeObserver(this);
          }
        };
    liveData.observeForever(observer);
    latch.await(2, TimeUnit.SECONDS);
    //noinspection unchecked
    return (T) data[0];
  }
}
