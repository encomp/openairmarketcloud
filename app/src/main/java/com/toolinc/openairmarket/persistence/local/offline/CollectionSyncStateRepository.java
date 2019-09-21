package com.toolinc.openairmarket.persistence.local.offline;

import androidx.lifecycle.LiveData;

import com.google.common.base.Optional;
import com.toolinc.openairmarket.common.inject.Global;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Collection sync state repository hides the async thread execution for insert, delete and
 * retrieval of information.
 */
public final class CollectionSyncStateRepository {

  private static final String TAG = CollectionSyncStateRepository.class.getSimpleName();
  private final CollectionSyncStateDao collectionSyncStateDao;
  private final Executor executor;

  @Inject
  public CollectionSyncStateRepository(
      @Global.NetworkIO Executor executor, CollectionSyncStateDao collectionSyncStateDao) {
    this.executor = executor;
    this.collectionSyncStateDao = collectionSyncStateDao;
  }

  public void insert(CollectionSyncState collectionSyncState) {
    Completable.fromAction(() -> collectionSyncStateDao.insert(collectionSyncState))
        .subscribeOn(Schedulers.from(executor))
        .doOnError(
            throwable -> {
              Timber.tag(TAG).e(throwable, "Unable to insert [%s].", collectionSyncState.getId());
            });
  }

  public void delete(CollectionSyncState collectionSyncState) {
    Completable.fromAction(() -> collectionSyncStateDao.delete(collectionSyncState))
        .subscribeOn(Schedulers.from(executor))
        .doOnError(
            throwable -> {
              Timber.tag(TAG).e(throwable, "Unable to delete [%s].", collectionSyncState.getId());
            });
  }

  public Optional<CollectionSyncState> findById(String id) {
    return Optional.fromNullable(collectionSyncStateDao.findById(id));
  }

  public LiveData<List<CollectionSyncState>> getAll() {
    return collectionSyncStateDao.getAll();
  }
}
