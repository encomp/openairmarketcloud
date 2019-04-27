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
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Categories;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.sync.DataSync;

/** Downloads the product category information from firestore database. */
public class ProductCategorySyncWorker extends Worker {

  private static final String TAG = ProductCategorySyncWorker.class.getSimpleName();

  private final Application application;
  private final CollectionSyncStateRepository collectionProductRepo;
  private final DataSync dataSync;

  @AssistedInject
  public ProductCategorySyncWorker(
      Application application,
      @Categories DataSync dataSync,
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
            .setCollectionId(CollectionsNames.PRODUCT_CATEGORIES)
            .setApplication(application)
            .setCollectionSyncStateRepository(collectionProductRepo)
            .setDataSync(dataSync)
            .build();
    return syncWorker.syncCollection();
  }

  @AssistedInject.Factory
  /**
   * Marker interface to support dependency injection for {@link ProductCategorySyncWorker}
   * instances.
   */
  public interface Factory extends ListenableWorkerFactory {}
}
