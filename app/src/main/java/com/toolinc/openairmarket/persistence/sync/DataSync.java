package com.toolinc.openairmarket.persistence.sync;

import android.content.Context;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import java.util.List;
import javax.inject.Inject;

/** Keeps the local data fresh. */
public class DataSync {
  private final SyncRepository syncRepository;
  private final ChannelProperties channelProperties;
  private final NotificationProperties startNotification;
  private final NotificationProperties successNotification;
  private final NotificationProperties failureNotification;

  @Inject
  public DataSync(
      SyncRepository syncRepository,
      ChannelProperties channelProperties,
      NotificationProperties startNotification,
      NotificationProperties successNotification,
      NotificationProperties failureNotification) {
    this.syncRepository = syncRepository;
    this.channelProperties = channelProperties;
    this.startNotification = startNotification;
    this.successNotification = successNotification;
    this.failureNotification = failureNotification;
  }

  public Task<QuerySnapshot> refresh(Context context) {
    return syncRepository.retrieveAll();
  }

  public ChannelProperties channelProperties() {
    return channelProperties;
  }

  public NotificationProperties startNotification() {
    return startNotification;
  }

  public NotificationProperties successNotification() {
    return successNotification;
  }

  public NotificationProperties failureNotification() {
    return failureNotification;
  }

  public void store(List<DocumentSnapshot> documentSnapshots) {
  }
}
