package com.toolinc.openairmarket.work;

import android.content.Context;
import androidx.work.ListenableWorker;
import androidx.work.ListenableWorker.Result;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.firebase.firestore.QuerySnapshot;
import com.toolinc.openairmarket.common.NotificationUtil;
import com.toolinc.openairmarket.persistence.local.database.model.CollectionSyncState;
import com.toolinc.openairmarket.persistence.local.database.dao.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.local.database.model.SyncStatus;
import com.toolinc.openairmarket.persistence.sync.DataSync;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;
import org.joda.time.DateTime;
import timber.log.Timber;

/**
 * Perform the sync of a collection from firestore and stored the latest update on the offline
 * database.
 */
@AutoValue
abstract class SyncWorker {

  private static final String TAG = SyncWorker.class.getSimpleName();
  private static final int THRESHOLD = 12;

  abstract String collectionId();

  abstract Context context();

  abstract CollectionSyncStateRepository collectionSyncStateRepository();

  abstract DataSync dataSync();

  @Nullable
  abstract ImmutableList<OneTimeWorkRequest> oneTimeWorkRequests();

  /**
   * Performs the synchronization of the collection {@link #collectionId()} if it has not been
   * performed in the last {@link #THRESHOLD} hours. If the data is not stale then the
   * synchronization will not be performed.
   */
  public Result syncCollection() {
    Optional<CollectionSyncState> css = collectionSyncStateRepository().findById(collectionId());
    if (css.isPresent()) {
      DateTime thresholdDateTime = DateTime.now().minusHours(THRESHOLD);
      if ((SyncStatus.FAILED.equals(css.get().getStatus())
          || SyncStatus.IN_PROGRESS.equals(css.get().getStatus()))
          || (SyncStatus.COMPLETE.equals(css.get().getStatus()) &&
          css.get().getLastUpdate().compareTo(thresholdDateTime) <= 0)) {
        return syncFromFirestore();
      }
    } else {
      return syncFromFirestore();
    }
    maybeScheduleSyncTasks();
    return Result.success();
  }

  private ListenableWorker.Result syncFromFirestore() {
    NotificationUtil.createChannel(context(), dataSync().channelProperties());
    updateSyncState(SyncStatus.IN_PROGRESS);
    NotificationUtil.notify(context(), dataSync().startNotification());
    Task<QuerySnapshot> task = dataSync().refresh(context());
    Timber.tag(TAG).d("Waiting for the task to complete...");
    try {
      Tasks.await(task);
      if (task.isSuccessful()) {
        Timber.tag(TAG).d("Sync was completed.");
        updateSyncState(SyncStatus.COMPLETE, task.getResult().size());
        dataSync().store(task.getResult().getDocuments());
        NotificationUtil.notify(context(), dataSync().successNotification());
        maybeScheduleSyncTasks();
        return Result.success();
      }
    } catch (ExecutionException | InterruptedException exc) {
      Timber.tag(TAG).e(exc, "Sync failed.");
      updateSyncState(SyncStatus.FAILED);
      NotificationUtil.notify(context(), dataSync().failureNotification());
    }
    return Result.failure();
  }

  private void maybeScheduleSyncTasks() {
    if (oneTimeWorkRequests() != null && oneTimeWorkRequests().size() > 0) {
      WorkManager.getInstance(context()).enqueue(oneTimeWorkRequests());
    }
  }

  private void updateSyncState(SyncStatus syncStatus) {
    updateSyncState(syncStatus, null);
  }

  private void updateSyncState(SyncStatus syncStatus, @Nullable Integer numberOfDocs) {
    Timber.tag(TAG).d("Updating the database.");
    CollectionSyncState collectionSyncState = new CollectionSyncState();
    collectionSyncState.setId(collectionId());
    collectionSyncState.setStatus(syncStatus);
    collectionSyncState.setLastUpdate(DateTime.now());
    if (numberOfDocs != null) {
      collectionSyncState.setNumberOfDocs(numberOfDocs);
    }
    collectionSyncStateRepository().insert(collectionSyncState);
  }

  static Builder builder() {
    return new AutoValue_SyncWorker.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {

    public abstract Builder setCollectionId(String collectionId);

    public abstract Builder setContext(Context context);

    public abstract Builder setCollectionSyncStateRepository(
        CollectionSyncStateRepository collectionSyncStateRepository);

    public abstract Builder setDataSync(DataSync dataSync);

    public abstract Builder setOneTimeWorkRequests(
        @Nullable OneTimeWorkRequest... oneTimeWorkRequest);

    public abstract SyncWorker build();
  }
}
