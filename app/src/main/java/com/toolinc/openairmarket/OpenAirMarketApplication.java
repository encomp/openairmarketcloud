package com.toolinc.openairmarket;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.work.Configuration;
import androidx.work.WorkManager;

import com.toolinc.openairmarket.common.inject.AppModule;
import com.toolinc.openairmarket.common.work.WorkerFactoryDagger;
import com.toolinc.openairmarket.inject.DaggerOpenAirMarketInjector;
import com.toolinc.openairmarket.inject.OpenAirMarketInjector;
import com.toolinc.openairmarket.persistence.local.offline.OfflineDatabaseModule;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

/** Base class for maintaining global application state and provide injection to the entire app. */
public class OpenAirMarketApplication extends Application implements HasActivityInjector {

  private OpenAirMarketInjector openAirMarketInjector;
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  @Inject WorkerFactoryDagger workerFactory;

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
    openAirMarketInjector =
        DaggerOpenAirMarketInjector.builder()
            .appModule(new AppModule(this))
            .offlineDatabaseModule(new OfflineDatabaseModule(this))
            .build();
    openAirMarketInjector.inject(this);
    WorkManager.initialize(
        this, new Configuration.Builder().setWorkerFactory(workerFactory).build());
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }

  public OpenAirMarketInjector getOpenAirMarketInjector() {
    return openAirMarketInjector;
  }

  @Override
  public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }

  public static String toString(BigDecimal bigDecimal) {
    DecimalFormat moneyFormat = new DecimalFormat();
    moneyFormat.setMinimumFractionDigits(0);
    moneyFormat.setMaximumFractionDigits(4);
    moneyFormat.setMaximumIntegerDigits(10);
    moneyFormat.setMinimumIntegerDigits(0);
    return moneyFormat.format(bigDecimal);
  }
}
