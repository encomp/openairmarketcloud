package com.toolinc.openairmarket.common.inject;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/** Defines an application module to be able to inject {@link Application} instances. */
@Module
public class AppModule {

  private final Application application;

  public AppModule(Application application) {
    this.application = application;
  }

  @Singleton
  @Provides
  Application providesApp() {
    return application;
  }
}
