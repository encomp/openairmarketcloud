package com.toolinc.openairmarket.persistence.sync;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.QuerySnapshot;
import com.toolinc.openairmarket.common.NotificationUtil;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.common.inject.Global;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.local.offline.CollectionState;
import com.toolinc.openairmarket.persistence.local.offline.CollectionStateRepository;

import org.joda.time.DateTime;

import java.util.concurrent.Executor;

import javax.inject.Inject;

/** Keeps the product brand data fresh. */
public final class DataSync {

  private final Executor executor;
  private final SyncRepository syncRepository;
  private final CollectionStateRepository collectionStateRepository;
  private final ChannelProperties channelProperties;
  private final NotificationProperties startNoti;
  private final NotificationProperties successNoti;
  private final NotificationProperties failureNoti;
  private Context context;

  @Inject
  public DataSync(
      @Global.NetworkIO Executor executor,
      SyncRepository syncRepository,
      CollectionStateRepository collectionStateRepository,
      ChannelProperties channelProperties,
      NotificationProperties startNoti,
      NotificationProperties successNoti,
      NotificationProperties failureNoti) {
    this.executor = executor;
    this.syncRepository = syncRepository;
    this.collectionStateRepository = collectionStateRepository;
    this.channelProperties = channelProperties;
    this.startNoti = startNoti;
    this.successNoti = successNoti;
    this.failureNoti = failureNoti;
  }

  private static final CollectionState create(String status) {
    CollectionState collectionState = new CollectionState();
    collectionState.setId("");
    collectionState.setStatus(status);
    collectionState.setLastUpdate(DateTime.now());
    collectionState.setNumberOfDocs(0);
    return collectionState;
  }

  public void refresh(Context context) {
    this.context = context;
    CollectionState collectionState = create("IN PROGRESS");
    collectionStateRepository.insert(collectionState);
    NotificationUtil.createChannel(context, channelProperties);
    NotificationUtil.notify(context, startNoti);
    syncRepository
        .retrieveAll()
        .addOnSuccessListener(executor, this::onSuccess)
        .addOnFailureListener(executor, this::onFailure);
  }

  private void onSuccess(@NonNull QuerySnapshot querySnapshot) {
    CollectionState collectionState = create("COMPLETE");
    collectionState.setNumberOfDocs(querySnapshot.size());
    collectionStateRepository.insert(collectionState);
    NotificationUtil.notify(context, successNoti);
  }

  private void onFailure(@NonNull Exception var1) {
    CollectionState collectionState = create("FAILED");
    collectionStateRepository.insert(collectionState);
    NotificationUtil.notify(context, failureNoti);
  }
}
