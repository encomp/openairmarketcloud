package com.toolinc.openairmarket.work;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.hilt.Assisted;
import androidx.hilt.work.WorkerInject;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.sync.MeasureUnitDataSync;

/** Downloads the product measure units information from firestore database. */
public class ProductMeasureUnitSyncWorker extends Worker {

  private final Context context;
  private final CollectionSyncStateRepository collectionProductRepo;
  private final MeasureUnitDataSync measureUnitDataSync;

  @WorkerInject
  public ProductMeasureUnitSyncWorker(
      MeasureUnitDataSync measureUnitDataSync,
      CollectionSyncStateRepository collectionProductRepo,
      @Assisted Context context,
      @Assisted WorkerParameters workerParameters) {
    super(context, workerParameters);
    this.measureUnitDataSync = measureUnitDataSync;
    this.collectionProductRepo = collectionProductRepo;
    this.context = context;
  }

  @NonNull
  @Override
  public Result doWork() {
    SyncWorker syncWorker =
        SyncWorker.builder()
            .setCollectionId(CollectionsNames.PRODUCT_UNITS)
            .setContext(context)
            .setCollectionSyncStateRepository(collectionProductRepo)
            .setDataSync(measureUnitDataSync)
            .setOneTimeWorkRequests(FirestoreSync.syncProductBrandRequest())
            .build();
    return syncWorker.syncCollection();
  }
}
