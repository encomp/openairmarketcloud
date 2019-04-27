package com.toolinc.openairmarket.persistence.sync;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.toolinc.openairmarket.common.NotificationUtil;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.common.inject.Global;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncState;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.local.offline.SyncStatus;

import org.joda.time.DateTime;

import java.util.concurrent.Executor;

import javax.inject.Inject;

/** Keeps the product brand data fresh. */
public final class DataSync {

  private final Executor executor;
  private final String collectionName;
  private final SyncRepository syncRepository;
  private final CollectionSyncStateRepository collectionSyncStateRepository;
  private final ChannelProperties channelProperties;
  private final NotificationProperties startNoti;
  private final NotificationProperties successNoti;
  private final NotificationProperties failureNoti;
  private Context context;

  @Inject
  public DataSync(
      @Global.NetworkIO Executor executor,
      String collectionName,
      SyncRepository syncRepository,
      CollectionSyncStateRepository collectionSyncStateRepository,
      ChannelProperties channelProperties,
      NotificationProperties startNoti,
      NotificationProperties successNoti,
      NotificationProperties failureNoti) {
    this.executor = executor;
    this.collectionName = collectionName;
    this.syncRepository = syncRepository;
    this.collectionSyncStateRepository = collectionSyncStateRepository;
    this.channelProperties = channelProperties;
    this.startNoti = startNoti;
    this.successNoti = successNoti;
    this.failureNoti = failureNoti;
  }

  public Task<QuerySnapshot> refresh(Context context) {
    this.context = context;
    CollectionSyncState collectionSyncState = create(SyncStatus.IN_PROGRESS);
    collectionSyncStateRepository.insert(collectionSyncState);
    NotificationUtil.createChannel(context, channelProperties);
    NotificationUtil.notify(context, startNoti);
    Task<QuerySnapshot> task = syncRepository.retrieveAll();
    task.addOnSuccessListener(executor, this::onSuccess);
    task.addOnFailureListener(executor, this::onFailure);
    return task;
  }

  private void onSuccess(@NonNull QuerySnapshot querySnapshot) {
    CollectionSyncState collectionSyncState = create(SyncStatus.COMPLETE);
    collectionSyncState.setNumberOfDocs(querySnapshot.size());
    collectionSyncStateRepository.insert(collectionSyncState);
    NotificationUtil.notify(context, successNoti);
  }

  private void onFailure(@NonNull Exception var1) {
    CollectionSyncState collectionSyncState = create(SyncStatus.FAILED);
    collectionSyncStateRepository.insert(collectionSyncState);
    NotificationUtil.notify(context, failureNoti);
  }

  private final CollectionSyncState create(SyncStatus status) {
    CollectionSyncState collectionSyncState = new CollectionSyncState();
    collectionSyncState.setId(collectionName);
    collectionSyncState.setStatus(status);
    collectionSyncState.setLastUpdate(DateTime.now());
    collectionSyncState.setNumberOfDocs(0);
    return collectionSyncState;
  }
}
