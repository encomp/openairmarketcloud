package com.toolinc.openairmarket.persistence.local.offline;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.io.Serializable;
import java.util.List;

/** Specifies the data access object behavior for the {@link CollectionSyncState}. */
@Dao
interface CollectionSyncStateDao extends Serializable {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(CollectionSyncState collectionSyncState);

  @Delete
  void delete(CollectionSyncState collectionSyncState);

  @Query("DELETE FROM CollectionSyncState")
  void deleteAll();

  @Query("SELECT * FROM CollectionSyncState WHERE id = :id")
  LiveData<CollectionSyncState> findById(String id);

  @Query("SELECT * from CollectionSyncState ORDER BY id ASC")
  LiveData<List<CollectionSyncState>> getAll();
}
