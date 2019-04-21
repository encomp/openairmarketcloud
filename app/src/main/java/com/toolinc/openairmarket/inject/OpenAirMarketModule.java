package com.toolinc.openairmarket.inject;

import com.google.firebase.auth.FirebaseAuth;

import java.math.BigDecimal;

import dagger.Module;
import dagger.Provides;

/** Provides the basic dependency injection for the app. */
@Module
public abstract class OpenAirMarketModule {

  @Provides
  static FirebaseAuth providesFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }
}
