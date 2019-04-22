package com.toolinc.openairmarket.persistence;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.QuerySnapshot;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.common.NotificationUtil;
import com.toolinc.openairmarket.persistence.offline.room.CollectionState;
import com.toolinc.openairmarket.persistence.offline.room.CollectionStateRepository;

import javax.inject.Inject;

/** Keeps the product brand data fresh. */
public final class ProductBrandOfflineRefresher {
  private static final String OFFLINE_ID = "ProductBrandOffline";
  private static final String CHANNEL_ID = "OFFLINE";
  private static final int NOTIFICATION_ID = 1;
  private final ProductBrandRepository productBrandRepository;
  private final CollectionStateRepository collectionStateRepository;
  private Context context;

  @Inject
  public ProductBrandOfflineRefresher(
      ProductBrandRepository productBrandRepository,
      CollectionStateRepository collectionStateRepository) {
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
    productBrandRepository.retrieveAll(this::onSuccess);
  }

  private final void onSuccess(@NonNull QuerySnapshot querySnapshot) {
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

  private static final CollectionState create(String status) {
    CollectionState collectionState = new CollectionState();
    collectionState.setId(OFFLINE_ID);
    collectionState.setStatus(status);
    collectionState.setLastUpdate("");
    collectionState.setNumberOfDocs(0);
    return collectionState;
  }
}
