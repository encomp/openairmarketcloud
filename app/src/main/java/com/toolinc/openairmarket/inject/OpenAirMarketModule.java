package com.toolinc.openairmarket.inject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.toolinc.openairmarket.common.inject.AppModule;
import com.toolinc.openairmarket.common.inject.AssitInjectionModule;
import com.toolinc.openairmarket.common.inject.ExecutorsModule;
import com.toolinc.openairmarket.persistence.cloud.inject.RepositoryModule;
import com.toolinc.openairmarket.ui.fragment.inject.SaleNotificationModule;
import com.toolinc.openairmarket.viewmodel.inject.ViewModelModule;
import com.toolinc.openairmarket.work.inject.WorkersModule;

import dagger.Module;
import dagger.Provides;

/** Provides the basic dependency injection for the app. */
@Module(
    includes = {
      AppModule.class,
      AssitInjectionModule.class,
      ExecutorsModule.class,
      RepositoryModule.class,
      SaleNotificationModule.class,
      ViewModelModule.class,
      WorkersModule.class
    })
public class OpenAirMarketModule {

  @Provides
  FirebaseAuth providesFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }

  @Provides
  FirebaseUser providesCurrentUserEmail(FirebaseAuth firebaseAuth) {
    return firebaseAuth.getCurrentUser();
  }

  @Provides
  FirebaseFirestore providesFirebaseFirestore() {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    firebaseFirestore.setFirestoreSettings(
        new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build());
    return firebaseFirestore;
  }
}
