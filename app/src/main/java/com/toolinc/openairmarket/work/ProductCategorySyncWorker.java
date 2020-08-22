package com.toolinc.openairmarket.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.Assisted;
import androidx.hilt.work.WorkerInject;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Categories;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomCategoryDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomCategory;
import com.toolinc.openairmarket.persistence.sync.DataSync;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductCategory;
import java.util.List;

/** Downloads the product category information from firestore database. */
public class ProductCategorySyncWorker extends Worker {

  private static final String TAG = ProductCategorySyncWorker.class.getSimpleName();

  private final Context context;
  private final CollectionSyncStateRepository collectionProductRepo;
  private final DataSync dataSync;
  private final ProductRoomCategoryDao productRoomCategoryDao;

  @WorkerInject
  public ProductCategorySyncWorker(
      @Categories DataSync dataSync,
      ProductRoomCategoryDao productRoomDao,
      CollectionSyncStateRepository collectionProductRepo,
      @Assisted Context context,
      @Assisted WorkerParameters workerParameters) {
    super(context, workerParameters);
    this.context = context;
    this.collectionProductRepo = collectionProductRepo;
    this.productRoomCategoryDao = productRoomDao;
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
            .setStoreSyncData(documentSnapshots -> {
              List<ProductCategory> productCategories = SyncRepository.toProductCategories(documentSnapshots);
              for (ProductCategory productCategory : productCategories) {
                ProductRoomCategory.Builder builder = ProductRoomCategory.builder();
                builder.setProductCategory(productCategory);
                productRoomCategoryDao.insert(builder.build());
              }
            })
            .build();
    return syncWorker.syncCollection();
  }
}
