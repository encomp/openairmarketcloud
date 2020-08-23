package com.toolinc.openairmarket;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.Configuration;

import com.google.common.base.Preconditions;

import java.util.concurrent.atomic.AtomicReference;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;

/** Base class for maintaining global application state and provide injection to the entire app. */
@HiltAndroidApp
public class OpenAirMarketApplication extends Application implements Configuration.Provider {

  // TODO: Doest work yet WorkerFactory Injection...
  //   https://developer.android.com/training/dependency-injection/hilt-jetpack#workmanager
  private static AtomicReference<HiltWorkerFactory> workerFactory = new AtomicReference<>();

  /** Determines if there is network connection available on the device. */
  public static boolean isInternetAvailable(Context context) {
    ConnectivityManager mConMgr =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    return mConMgr != null
        && mConMgr.getActiveNetworkInfo() != null
        && mConMgr.getActiveNetworkInfo().isConnected();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }

  @NonNull
  @Override
  public Configuration getWorkManagerConfiguration() {
    Configuration.Builder builder = new Configuration.Builder();
    if (workerFactory.get() != null) {
      builder.setWorkerFactory(workerFactory.get());
    }
    return builder.build();
  }

  public static void setWorkerFactory(HiltWorkerFactory workerFactory) {
    if (workerFactory != null && !workerFactory
        .equals(OpenAirMarketApplication.workerFactory.get())) {
      OpenAirMarketApplication.workerFactory.set(workerFactory);
    }
  }

  public static String toString(BigDecimal bigDecimal) {
    DecimalFormat moneyFormat = new DecimalFormat();
    moneyFormat.setMinimumFractionDigits(0);
    moneyFormat.setMaximumFractionDigits(4);
    moneyFormat.setMaximumIntegerDigits(10);
    moneyFormat.setMinimumIntegerDigits(0);
    return moneyFormat.format(bigDecimal);
  }

  public static Date toDate(DateTime dateTime) {
    Preconditions.checkNotNull(dateTime);
    if (DateTimeZone.UTC.equals(dateTime.getZone())) {
      return dateTime.toDate();
    }
    return dateTime.toDateTime(DateTimeZone.UTC).toDate();
  }
}
