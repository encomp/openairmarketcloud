package com.toolinc.openairmarket.work;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.hilt.Assisted;
import androidx.hilt.work.WorkerInject;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.local.database.dao.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomCategoryDao;
import com.toolinc.openairmarket.persistence.sync.CategoryDataSync;

/** Downloads the product category information from firestore database. */
public class ProductCategorySyncWorker extends Worker {

  private final Context context;
  private final CollectionSyncStateRepository collectionProductRepo;
  private final CategoryDataSync categoryDataSync;
  private final ProductRoomCategoryDao productRoomCategoryDao;

  @WorkerInject
  public ProductCategorySyncWorker(
      CategoryDataSync categoryDataSync,
      ProductRoomCategoryDao productRoomDao,
      CollectionSyncStateRepository collectionProductRepo,
      @Assisted Context context,
      @Assisted WorkerParameters workerParameters) {
    super(context, workerParameters);
    this.context = context;
    this.collectionProductRepo = collectionProductRepo;
    this.productRoomCategoryDao = productRoomDao;
    this.categoryDataSync = categoryDataSync;
  }

  @NonNull
  @Override
  public Result doWork() {
    SyncWorker syncWorker =
        SyncWorker.builder()
            .setCollectionId(CollectionsNames.PRODUCT_CATEGORIES)
            .setContext(context)
            .setCollectionSyncStateRepository(collectionProductRepo)
            .setDataSync(categoryDataSync)
            .build();
    return syncWorker.syncCollection();
  }
}
