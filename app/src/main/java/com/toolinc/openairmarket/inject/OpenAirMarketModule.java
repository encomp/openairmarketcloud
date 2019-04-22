package com.toolinc.openairmarket.inject;

import androidx.work.ListenableWorker;

import com.google.common.collect.ImmutableMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.toolinc.openairmarket.common.inject.AssitInjectionModule;
import com.toolinc.openairmarket.common.inject.ExecutorsModule;
import com.toolinc.openairmarket.persistence.PersistenceModule;
import com.toolinc.openairmarket.work.ListenableWorkerFactory;
import com.toolinc.openairmarket.work.ProductWorker;
import com.toolinc.openairmarket.work.WorkerFactoryDagger;

import javax.inject.Provider;

import dagger.Module;
import dagger.Provides;

/** Provides the basic dependency injection for the app. */
@Module(includes = {AssitInjectionModule.class, ExecutorsModule.class, PersistenceModule.class})
public abstract class OpenAirMarketModule {

  @Provides
  static FirebaseAuth providesFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }

  @Provides
  static FirebaseFirestore providesFirebaseFirestore() {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    firebaseFirestore.setFirestoreSettings(
        new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build());
    return firebaseFirestore;
  }

  /** Provides a {@link androidx.work.WorkerFactory} instance that support dependency injection. */
  abstract WorkerFactoryDagger workerFactory();

  @Provides
  static ImmutableMap<Class<ListenableWorker>, Provider<ListenableWorkerFactory>>
      providesWorkerFactories(Provider<ProductWorker.Factory> productWorkerProvider) {
    ImmutableMap.Builder<Class<ListenableWorker>, Provider<ListenableWorkerFactory>> builder =
        ImmutableMap.builder();
    builder.put(toClass(ProductWorker.class), toType(productWorkerProvider));
    return builder.build();
  }

  @SuppressWarnings("unchecked")
  private static final <T> Class<T> toClass(Class<?> provider) {
    return (Class<T>) provider;
  }

  @SuppressWarnings("unchecked")
  private static final <T> Provider<T> toType(Provider<?> provider) {
    return (Provider<T>) provider;
  }
}
