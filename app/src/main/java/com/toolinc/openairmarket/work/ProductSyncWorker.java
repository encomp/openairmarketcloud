package com.toolinc.openairmarket.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.Assisted;
import androidx.hilt.work.WorkerInject;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Products;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.sync.DataSync;

/** Downloads the product information from firestore database. */
public class ProductSyncWorker extends Worker {

  private static final String TAG = ProductSyncWorker.class.getSimpleName();

  private final Context context;
  private final CollectionSyncStateRepository collectionProductRepo;
  private final DataSync dataSync;

  @WorkerInject
  public ProductSyncWorker(
      @Products DataSync dataSync,
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
            .setCollectionId(CollectionsNames.PRODUCTS)
            .setContext(context)
            .setCollectionSyncStateRepository(collectionProductRepo)
            .setDataSync(dataSync)
            .build();
    return syncWorker.syncCollection();
  }
}
