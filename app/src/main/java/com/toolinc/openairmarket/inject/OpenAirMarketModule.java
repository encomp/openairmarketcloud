package com.toolinc.openairmarket.inject;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Provides;

/** Provides the basic dependency injection for the app. */
public class OpenAirMarketModule {

  @Provides
  FirebaseAuth providesFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }
}
