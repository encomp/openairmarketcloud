package com.toolinc.openairmarket.persistence.local.database.dao;

import androidx.lifecycle.LiveData;

import com.google.common.base.Optional;

import com.toolinc.openairmarket.persistence.local.database.model.CollectionSyncState;
import java.util.List;

import javax.inject.Inject;

/**
 * Collection sync state repository hides the async thread execution for insert, delete and
 * retrieval of information.
 */
public final class CollectionSyncStateRepository {

  private static final String TAG = CollectionSyncStateRepository.class.getSimpleName();
  private final CollectionSyncStateDao collectionSyncStateDao;

  @Inject
  public CollectionSyncStateRepository(CollectionSyncStateDao collectionSyncStateDao) {
    this.collectionSyncStateDao = collectionSyncStateDao;
  }

  public void insert(CollectionSyncState collectionSyncState) {
    collectionSyncStateDao.insert(collectionSyncState);
  }

  public void delete(CollectionSyncState collectionSyncState) {
    collectionSyncStateDao.delete(collectionSyncState);
  }

  public Optional<CollectionSyncState> findById(String id) {
    return Optional.fromNullable(collectionSyncStateDao.findById(id));
  }

  public LiveData<List<CollectionSyncState>> getAll() {
    return collectionSyncStateDao.getAll();
  }
}
