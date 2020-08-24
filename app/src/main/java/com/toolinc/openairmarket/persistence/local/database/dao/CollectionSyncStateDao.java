package com.toolinc.openairmarket.persistence.local.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.toolinc.openairmarket.persistence.local.database.model.CollectionSyncState;
import java.io.Serializable;
import java.util.List;

/** Specifies the data access object behavior for the {@link CollectionSyncState}. */
@Dao
public interface CollectionSyncStateDao extends Serializable {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(CollectionSyncState collectionSyncState);

  @Delete
  void delete(CollectionSyncState collectionSyncState);

  @Query("SELECT * FROM CollectionSyncState WHERE id = :id")
  CollectionSyncState findById(String id);

  @Query("SELECT * from CollectionSyncState ORDER BY id ASC")
  LiveData<List<CollectionSyncState>> getAll();
}
