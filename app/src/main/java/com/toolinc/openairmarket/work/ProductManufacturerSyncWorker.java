package com.toolinc.openairmarket.work;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.hilt.Assisted;
import androidx.hilt.work.WorkerInject;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.local.database.dao.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.sync.ManufacturerDataSync;

/** Downloads the product manufacturer information from firestore database. */
public class ProductManufacturerSyncWorker extends Worker {

  private final Context context;
  private final CollectionSyncStateRepository collectionProductRepo;
  private final ManufacturerDataSync manufacturerDataSync;

  @WorkerInject
  public ProductManufacturerSyncWorker(
      ManufacturerDataSync manufacturerDataSync,
      CollectionSyncStateRepository collectionProductRepo,
      @Assisted Context context,
      @Assisted WorkerParameters workerParameters) {
    super(context, workerParameters);
    this.manufacturerDataSync = manufacturerDataSync;
    this.collectionProductRepo = collectionProductRepo;
    this.context = context;
  }

  @NonNull
  @Override
  public Result doWork() {
    SyncWorker syncWorker =
        SyncWorker.builder()
            .setCollectionId(CollectionsNames.PRODUCT_MANUFACTURERS)
            .setContext(context)
            .setCollectionSyncStateRepository(collectionProductRepo)
            .setDataSync(manufacturerDataSync)
            .setOneTimeWorkRequests(FirestoreSync.syncProductBrandRequest())
            .build();
    return syncWorker.syncCollection();
  }
}
