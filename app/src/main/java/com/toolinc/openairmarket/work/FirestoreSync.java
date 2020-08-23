package com.toolinc.openairmarket.work;

import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Helper class that provide the {@link OneTimeWorkRequest} that sync information from firestore and
 * stored the data locally on a Room database.
 **/
public final class FirestoreSync {

  private FirestoreSync() {}

  public static List<OneTimeWorkRequest> syncCatalogData() {
    return ImmutableList.of(syncProductMeasureUnitRequest(), syncProductManufacturerRequest(),
        syncProductCategoryRequest());
  }

  public static OneTimeWorkRequest syncProductBrandRequest() {
    return new OneTimeWorkRequest.Builder(ProductBrandSyncWorker.class)
        .setInitialDelay(500, TimeUnit.MILLISECONDS)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS)
        .build();
  }

  private static OneTimeWorkRequest syncProductCategoryRequest() {
    return new OneTimeWorkRequest.Builder(ProductCategorySyncWorker.class)
        .setInitialDelay(2, TimeUnit.SECONDS)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS)
        .build();
  }

  private static OneTimeWorkRequest syncProductManufacturerRequest() {
    return new OneTimeWorkRequest.Builder(ProductManufacturerSyncWorker.class)
        .setInitialDelay(1, TimeUnit.SECONDS)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS)
        .build();
  }

  private static OneTimeWorkRequest syncProductMeasureUnitRequest() {
    return new OneTimeWorkRequest.Builder(ProductMeasureUnitSyncWorker.class)
        .setInitialDelay(500, TimeUnit.MILLISECONDS)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS)
        .build();
  }

  private static OneTimeWorkRequest syncProductRequest() {
    return new OneTimeWorkRequest.Builder(ProductSyncWorker.class)
        .setInitialDelay(300, TimeUnit.MILLISECONDS)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS)
        .build();
  }
}
