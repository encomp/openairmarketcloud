package com.toolinc.openairmarket.inject;

import com.toolinc.openairmarket.ui.EmailPasswordActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Specifies the object graph to inject different instances to activities and fragments of the
 * application.
 */
@Module
public abstract class OpenAirMarketModule {
  @ContributesAndroidInjector
  abstract EmailPasswordActivity emailPasswordActivity();
}
