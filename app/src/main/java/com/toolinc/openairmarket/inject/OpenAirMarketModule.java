package com.toolinc.openairmarket.inject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.toolinc.openairmarket.common.inject.ExecutorsModule;
import com.toolinc.openairmarket.persistence.local.offline.OfflineDatabaseModule;
import com.toolinc.openairmarket.persistence.local.pos.PosDatabaseModule;
import com.toolinc.openairmarket.ui.fragment.inject.SaleNotificationModule;
import com.toolinc.openairmarket.viewmodel.inject.ViewModelModule;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

/** Provides the basic dependency injection for the app. */
@InstallIn(ApplicationComponent.class)
@Module(
    includes = {
      ExecutorsModule.class,
      OfflineDatabaseModule.class,
      PosDatabaseModule.class,
      SaleNotificationModule.class,
      ViewModelModule.class,
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
