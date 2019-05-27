package com.toolinc.openairmarket.work;

import android.app.Application;

import androidx.work.ListenableWorker;
import androidx.work.ListenableWorker.Result;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import com.google.firebase.firestore.QuerySnapshot;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncState;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.sync.DataSync;

import org.joda.time.DateTime;

import java.util.concurrent.ExecutionException;

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

  abstract Application application();

  abstract CollectionSyncStateRepository collectionSyncStateRepository();

  abstract DataSync dataSync();

  /**
   * Performs the synchronization of the collection {@link #collectionId()} if it has not been
   * performed in the last {@link #THRESHOLD} hours. If the data is not stale then the
   * synchronization will not be performed.
   */
  public Result syncCollection() {
    Optional<CollectionSyncState> css = collectionSyncStateRepository().findById(collectionId());
    if (css.isPresent()) {
      DateTime thresholdDateTime = DateTime.now().minusHours(THRESHOLD);
      if (css.get().getLastUpdate().compareTo(thresholdDateTime) <= 0) {
        return syncFromFirestore();
      }
    } else {
      return syncFromFirestore();
    }
    return Result.success();
  }

  private ListenableWorker.Result syncFromFirestore() {
    Task<QuerySnapshot> task = dataSync().refresh(application().getApplicationContext());
    Timber.tag(TAG).d("Waiting for the task to complete...");
    try {
      Tasks.await(task);
      if (task.isSuccessful()) {
        return Result.success();
      }
    } catch (ExecutionException | InterruptedException exc) {
      Timber.tag(TAG).e(exc);
    }
    return Result.failure();
  }

  static Builder builder() {
    return new AutoValue_SyncWorker.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {

    abstract Builder setCollectionId(String collectionId);

    abstract Builder setApplication(Application application);

    abstract Builder setCollectionSyncStateRepository(
        CollectionSyncStateRepository collectionSyncStateRepository);

    abstract Builder setDataSync(DataSync dataSync);

    abstract SyncWorker build();
  }
}
