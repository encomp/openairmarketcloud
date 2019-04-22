package com.toolinc.openairmarket.inject;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;

import com.google.common.collect.ImmutableMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.toolinc.openairmarket.persistence.offline.room.OfflineDatabaseModule;
import com.toolinc.openairmarket.work.ListenableWorkerFactory;
import com.toolinc.openairmarket.work.ProductWorker;
import com.toolinc.openairmarket.work.WorkerFactoryDagger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/** Provides the basic dependency injection for the app. */
@Module(includes = {AssitInjectionModule.class, OfflineDatabaseModule.class})
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

  @Global.DiskIO
  @Singleton
  static Executor providesDiskIOExecutor() {
    return Executors.newSingleThreadExecutor();
  }

  @Global.NetworkIO
  @Singleton
  static Executor providesNetworkIOExecutor() {
    return Executors.newFixedThreadPool(3);
  }

  @Global.MainThread
  @Singleton
  static Executor providesMainThreadExecutor() {
    return new MainThreadExecutor();
  }

  @Singleton
  private static class MainThreadExecutor implements Executor {
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable command) {
      mainThreadHandler.post(command);
    }
  }
}
