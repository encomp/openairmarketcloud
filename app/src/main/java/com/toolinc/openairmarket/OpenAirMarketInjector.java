package com.toolinc.openairmarket;

import com.toolinc.openairmarket.inject.OpenAirMarketModule;
import com.toolinc.openairmarket.inject.OpenAirMarketUiModule;

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
public interface OpenAirMarketInjector extends AndroidInjector<OpenAirMarketApplication> {}
