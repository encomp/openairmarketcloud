package com.toolinc.openairmarket.work.inject;

import androidx.work.ListenableWorker;

import com.google.common.collect.ImmutableMap;
import com.toolinc.openairmarket.common.work.ListenableWorkerFactory;
import com.toolinc.openairmarket.common.work.inject.WorkerFactoryDaggerModule;
import com.toolinc.openairmarket.work.ProductWorker;

import javax.inject.Provider;

import dagger.Module;
import dagger.Provides;

@Module(includes = WorkerFactoryDaggerModule.class)
public class WorkersModule {

  @SuppressWarnings("unchecked")
  private static final <T> Class<T> toClass(Class<?> provider) {
    return (Class<T>) provider;
  }

  @SuppressWarnings("unchecked")
  private static final <T> Provider<T> toType(Provider<?> provider) {
    return (Provider<T>) provider;
  }

  @Provides
  ImmutableMap<Class<ListenableWorker>, Provider<ListenableWorkerFactory>> providesWorkerFactories(
      Provider<ProductWorker.Factory> productWorkerProvider) {
    ImmutableMap.Builder<Class<ListenableWorker>, Provider<ListenableWorkerFactory>> builder =
        ImmutableMap.builder();
    builder.put(toClass(ProductWorker.class), toType(productWorkerProvider));
    return builder.build();
  }
}
