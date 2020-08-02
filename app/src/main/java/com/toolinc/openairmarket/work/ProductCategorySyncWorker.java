package com.toolinc.openairmarket.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.Assisted;
import androidx.hilt.work.WorkerInject;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Categories;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.sync.DataSync;

/** Downloads the product category information from firestore database. */
public class ProductCategorySyncWorker extends Worker {

  private static final String TAG = ProductCategorySyncWorker.class.getSimpleName();

  private final Context context;
  private final CollectionSyncStateRepository collectionProductRepo;
  private final DataSync dataSync;

  @WorkerInject
  public ProductCategorySyncWorker(
      @Categories DataSync dataSync,
      CollectionSyncStateRepository collectionProductRepo,
      @Assisted Context context,
      @Assisted WorkerParameters workerParameters) {
    super(context, workerParameters);
    this.context = context;
    this.collectionProductRepo = collectionProductRepo;
    this.dataSync = dataSync;
  }

  @NonNull
  @Override
  public Result doWork() {
    SyncWorker syncWorker =
        SyncWorker.builder()
            .setCollectionId(CollectionsNames.PRODUCT_CATEGORIES)
            .setContext(context)
            .setCollectionSyncStateRepository(collectionProductRepo)
            .setDataSync(dataSync)
            .build();
    return syncWorker.syncCollection();
  }
}
