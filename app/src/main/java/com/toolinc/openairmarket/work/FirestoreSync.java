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
    return ImmutableList.of(syncProductCategoryRequest(), syncProductManufacturerRequest());
  }

  public static OneTimeWorkRequest syncProductBrandRequest() {
    return new OneTimeWorkRequest.Builder(ProductBrandSyncWorker.class)
        .setInitialDelay(2000, TimeUnit.MILLISECONDS)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS)
        .build();
  }

  private static OneTimeWorkRequest syncProductCategoryRequest() {
    return new OneTimeWorkRequest.Builder(ProductCategorySyncWorker.class)
        .setInitialDelay(2000, TimeUnit.MILLISECONDS)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS)
        .build();
  }

  private static OneTimeWorkRequest syncProductManufacturerRequest() {
    return new OneTimeWorkRequest.Builder(ProductManufacturerSyncWorker.class)
        .setInitialDelay(2000, TimeUnit.MILLISECONDS)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS)
        .build();
  }

  private static OneTimeWorkRequest syncProductRequest() {
    return new OneTimeWorkRequest.Builder(ProductSyncWorker.class)
        .setInitialDelay(2000, TimeUnit.MILLISECONDS)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS)
        .build();
  }
}
