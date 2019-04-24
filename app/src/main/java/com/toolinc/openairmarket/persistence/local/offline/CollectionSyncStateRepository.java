package com.toolinc.openairmarket.persistence.local.offline;

import androidx.lifecycle.LiveData;

import com.toolinc.openairmarket.common.inject.Global;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

/**
 * Collection sync state repository hides the async thread execution for insert, delete and
 * retrieval of information.
 */
public final class CollectionSyncStateRepository {

  private final CollectionSyncStateDao collectionSyncStateDao;
  private final Executor executor;

  @Inject
  public CollectionSyncStateRepository(
      @Global.NetworkIO Executor executor, CollectionSyncStateDao collectionSyncStateDao) {
    this.executor = executor;
    this.collectionSyncStateDao = collectionSyncStateDao;
  }

  public void insert(CollectionSyncState collectionSyncState) {
    executor.execute(() -> collectionSyncStateDao.insert(collectionSyncState));
  }

  public void delete(CollectionSyncState collectionSyncState) {
    executor.execute(() -> collectionSyncStateDao.delete(collectionSyncState));
  }

  public LiveData<CollectionSyncState> findById(String id) {
    return collectionSyncStateDao.findById(id);
  }

  public LiveData<List<CollectionSyncState>> getAll() {
    return collectionSyncStateDao.getAll();
  }
}
