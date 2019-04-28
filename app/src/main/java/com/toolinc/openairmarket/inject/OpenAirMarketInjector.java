package com.toolinc.openairmarket.inject;

import com.toolinc.openairmarket.OpenAirMarketApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/** Provides dependency injection for the entire application from the specific modules provided. */
@Singleton
@Component(
    modules = {
      AndroidInjectionModule.class,
      OpenAirMarketModule.class,
      OpenAirMarketUiModule.class
    })
public interface OpenAirMarketInjector extends AndroidInjector<OpenAirMarketApplication> {

  void inject(OpenAirMarketApplication app);
}
