package com.toolinc.openairmarket.work;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.squareup.inject.assisted.Assisted;
import com.squareup.inject.assisted.AssistedInject;
import com.toolinc.openairmarket.common.work.ListenableWorkerFactory;
import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Products;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.sync.DataSync;

/** Downloads the product information from firestore database. */
public class ProductSyncWorker extends Worker {

  private static final String TAG = ProductSyncWorker.class.getSimpleName();

  private final Application application;
  private final CollectionSyncStateRepository collectionProductRepo;
  private final DataSync dataSync;

  @AssistedInject
  public ProductSyncWorker(
      Application application,
      @Products DataSync dataSync,
      CollectionSyncStateRepository collectionProductRepo,
      @Assisted Context context,
      @Assisted WorkerParameters workerParameters) {
    super(context, workerParameters);
    this.application = application;
    this.collectionProductRepo = collectionProductRepo;
    this.dataSync = dataSync;
  }

  @NonNull
  @Override
  public Result doWork() {
    SyncWorker syncWorker =
        SyncWorker.builder()
            .setCollectionId(CollectionsNames.PRODUCTS)
            .setApplication(application)
            .setCollectionSyncStateRepository(collectionProductRepo)
            .setDataSync(dataSync)
            .build();
    return syncWorker.syncCollection();
  }

  @AssistedInject.Factory
  /** Marker interface to support dependency injection for {@link ProductSyncWorker} instances. */
  public interface Factory extends ListenableWorkerFactory {}
}
