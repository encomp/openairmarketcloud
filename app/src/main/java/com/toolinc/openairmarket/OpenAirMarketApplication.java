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
import com.toolinc.openairmarket.persistence.local.offline.OfflineDatabaseModule;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

/** Base class for maintaining global application state and provide injection to the entire app. */
public class OpenAirMarketApplication extends Application implements HasActivityInjector {

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
    DaggerOpenAirMarketInjector.builder()
        .appModule(new AppModule(this))
        .offlineDatabaseModule(new OfflineDatabaseModule(this))
        .build()
        .inject(this);
    WorkManager.initialize(
        this, new Configuration.Builder().setWorkerFactory(workerFactory).build());
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }

  @Override
  public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }
}
