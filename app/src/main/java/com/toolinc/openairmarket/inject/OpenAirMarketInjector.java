package com.toolinc.openairmarket.inject;

import com.toolinc.openairmarket.OpenAirMarketApplication;
import com.toolinc.openairmarket.widget.SaleWidget;

import javax.inject.Singleton;

import dagger.Component;

/** Provides dependency injection for the entire application from the specific modules provided. */
@Singleton
@Component(modules = {OpenAirMarketModule.class})
public abstract class OpenAirMarketInjector {

  abstract void inject(OpenAirMarketApplication app);

  abstract void inject(SaleWidget saleWidget);
}
