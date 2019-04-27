package com.toolinc.openairmarket.work.inject;

import androidx.work.ListenableWorker;

import com.google.common.collect.ImmutableMap;
import com.toolinc.openairmarket.common.work.ListenableWorkerFactory;
import com.toolinc.openairmarket.common.work.inject.WorkerFactoryDaggerModule;
import com.toolinc.openairmarket.persistence.sync.inject.ProductDataSyncModule;
import com.toolinc.openairmarket.work.ProductCategorySyncWorker;
import com.toolinc.openairmarket.work.ProductSyncWorker;

import javax.inject.Provider;

import dagger.Module;
import dagger.Provides;

/** Specifies the injection for the worker managers. */
@Module(includes = {WorkerFactoryDaggerModule.class, ProductDataSyncModule.class})
public class WorkersModule {

  @SuppressWarnings("unchecked")
  private static final <T> Class<T> toClass(Class<?> provider) {
    return (Class<T>) provider;
  }

  @SuppressWarnings("unchecked")
  private static final <T> Provider<T> toType(Provider<?> provider) {
    return (Provider<T>) provider;
  }

  @SuppressWarnings("unchecked")
  @Provides
  ImmutableMap<Class<ListenableWorker>, Provider<ListenableWorkerFactory>> providesWorkerFactories(
      Provider<ProductCategorySyncWorker.Factory> productCategoryWorkerProvider,
      Provider<ProductSyncWorker.Factory> productWorkerProvider) {
    ImmutableMap.Builder<Class<ListenableWorker>, Provider<ListenableWorkerFactory>> builder =
        ImmutableMap.builder();
    builder.put(toClass(ProductCategorySyncWorker.class), toType(productCategoryWorkerProvider));
    builder.put(toClass(ProductSyncWorker.class), toType(productWorkerProvider));
    return builder.build();
  }
}
