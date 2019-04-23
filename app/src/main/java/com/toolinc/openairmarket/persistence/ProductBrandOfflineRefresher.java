package com.toolinc.openairmarket.persistence;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.QuerySnapshot;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.common.NotificationUtil;
import com.toolinc.openairmarket.common.inject.Global;
import com.toolinc.openairmarket.persistence.offline.room.CollectionState;
import com.toolinc.openairmarket.persistence.offline.room.CollectionStateRepository;

import java.util.concurrent.Executor;

import javax.inject.Inject;

/** Keeps the product brand data fresh. */
public final class ProductBrandOfflineRefresher {
  private static final String OFFLINE_ID = "ProductBrandOffline";
  private static final String CHANNEL_ID = "OFFLINE";
  private static final int NOTIFICATION_ID = 1;
  private final Executor executor;
  private final ProductBrandRepository productBrandRepository;
  private final CollectionStateRepository collectionStateRepository;
  private Context context;

  @Inject
  public ProductBrandOfflineRefresher(
      @Global.NetworkIO Executor executor,
      ProductBrandRepository productBrandRepository,
      CollectionStateRepository collectionStateRepository) {
    this.executor = executor;
    this.productBrandRepository = productBrandRepository;
    this.collectionStateRepository = collectionStateRepository;
  }

  public void refresh(Context context) {
    this.context = context;
    CollectionState collectionState = create("IN PROGRESS");
    collectionStateRepository.insert(collectionState);
    NotificationUtil.createChannel(
        context,
        CHANNEL_ID,
        context.getString(R.string.offline_channel_name),
        context.getString(R.string.offline_channel_description),
        NotificationManager.IMPORTANCE_HIGH);
    NotificationUtil.notify(
        context,
        NOTIFICATION_ID,
        CHANNEL_ID,
        R.drawable.ic_cloud_download,
        context.getString(R.string.product_brand_offline_notification_inprogress_title),
        context.getString(R.string.product_brand_offline_notification_inprogress_content),
        NotificationManager.IMPORTANCE_HIGH);
    productBrandRepository
        .retrieveAll()
        .addOnSuccessListener(executor, this::onSuccess)
        .addOnFailureListener(executor, this::onFailure);
  }

  private void onSuccess(@NonNull QuerySnapshot querySnapshot) {
    CollectionState collectionState = create("COMPLETE");
    collectionState.setNumberOfDocs(querySnapshot.size());
    collectionStateRepository.insert(collectionState);
    NotificationUtil.notify(
        context,
        NOTIFICATION_ID,
        CHANNEL_ID,
        R.drawable.ic_cloud_done,
        context.getString(R.string.product_brand_offline_notification_success_title),
        context.getString(R.string.product_brand_offline_notification_success_content),
        NotificationManager.IMPORTANCE_HIGH);
  }

  private void onFailure(@NonNull Exception var1) {
    CollectionState collectionState = create("FAILED");
    collectionStateRepository.insert(collectionState);
    NotificationUtil.notify(
            context,
            NOTIFICATION_ID,
            CHANNEL_ID,
            R.drawable.ic_cloud_off,
            context.getString(R.string.product_brand_offline_notification_failure_title),
            context.getString(R.string.product_brand_offline_notification_failure_content),
            NotificationManager.IMPORTANCE_HIGH);
  }

  private static final CollectionState create(String status) {
    CollectionState collectionState = new CollectionState();
    collectionState.setId(OFFLINE_ID);
    collectionState.setStatus(status);
    collectionState.setLastUpdate("");
    collectionState.setNumberOfDocs(0);
    return collectionState;
  }
}
