package com.toolinc.openairmarket;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/** Base class for maintaining global application state and provide injection to the entire app. */
public class OpenAirMarketApplication extends Application implements HasActivityInjector {

  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

  @Override
  public void onCreate() {
    super.onCreate();
    DaggerOpenAirMarketInjector.create().inject(this);
  }

  @Override
  public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }

  public static boolean isInternetAvailable(Context context) {
    ConnectivityManager mConMgr =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    return mConMgr != null
        && mConMgr.getActiveNetworkInfo() != null
        && mConMgr.getActiveNetworkInfo().isConnected();
  }
}
