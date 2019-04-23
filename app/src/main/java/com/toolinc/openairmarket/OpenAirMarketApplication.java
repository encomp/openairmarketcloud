package com.toolinc.openairmarket;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.work.Configuration;
import androidx.work.WorkManager;

import com.toolinc.openairmarket.persistence.local.offline.OfflineDatabaseModule;
import com.toolinc.openairmarket.common.work.WorkerFactoryDagger;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

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
        .offlineDatabaseModule(new OfflineDatabaseModule(this))
        .build()
        .inject(this);
    WorkManager.initialize(
        this, new Configuration.Builder().setWorkerFactory(workerFactory).build());
  }

  @Override
  public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }
}
