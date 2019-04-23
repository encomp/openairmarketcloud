package com.toolinc.openairmarket.persistence.local.offline;

import androidx.lifecycle.LiveData;

import com.toolinc.openairmarket.common.inject.Global;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

/**
 * Collection State repository hides the async thread execution for insert, delete and retrieval of
 * information.
 */
public final class CollectionStateRepository {

  private final CollectionStateDao collectionStateDao;
  private final Executor executor;

  @Inject
  public CollectionStateRepository(
      @Global.NetworkIO Executor executor, CollectionStateDao collectionStateDao) {
    this.executor = executor;
    this.collectionStateDao = collectionStateDao;
  }

  public void insert(CollectionState collectionState) {
    executor.execute(() -> collectionStateDao.insert(collectionState));
  }

  public void delete(CollectionState collectionState) {
    executor.execute(() -> collectionStateDao.delete(collectionState));
  }

  public LiveData<CollectionState> findById(String id) {
    return collectionStateDao.findById(id);
  }

  public LiveData<List<CollectionState>> getAll() {
    return collectionStateDao.getAll();
  }
}
