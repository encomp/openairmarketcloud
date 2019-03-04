package com.toolinc.openairmarket.inject;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Module;
import dagger.Provides;

/** Provides the basic dependency injection for the app. */
@Module
public class OpenAirMarketModule {

  @Provides
  static FirebaseAuth providesFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }
}
