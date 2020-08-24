package com.toolinc.openairmarket.work;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.hilt.Assisted;
import androidx.hilt.work.WorkerInject;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.local.database.dao.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.sync.BrandDataSync;

/** Downloads the product brand information from firestore database. */
public class ProductBrandSyncWorker extends Worker {

  private final Context context;
  private final CollectionSyncStateRepository collectionProductRepo;
  private final BrandDataSync brandDataSync;

  @WorkerInject
  public ProductBrandSyncWorker(
      BrandDataSync brandDataSync,
      CollectionSyncStateRepository collectionProductRepo,
      @Assisted Context context,
      @Assisted WorkerParameters workerParameters) {
    super(context, workerParameters);
    this.brandDataSync = brandDataSync;
    this.collectionProductRepo = collectionProductRepo;
    this.context = context;
  }

  @NonNull
  @Override
  public Result doWork() {
    SyncWorker syncWorker =
        SyncWorker.builder()
            .setCollectionId(CollectionsNames.PRODUCT_BRANDS)
            .setContext(context)
            .setCollectionSyncStateRepository(collectionProductRepo)
            .setDataSync(brandDataSync)
            .build();
    return syncWorker.syncCollection();
  }
}
