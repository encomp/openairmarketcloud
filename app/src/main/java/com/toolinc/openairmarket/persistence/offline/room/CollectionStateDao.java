package com.toolinc.openairmarket.persistence.offline.room;

import android.os.Parcelable;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.io.Serializable;
import java.util.List;

/** Specifies the data access object behavior for the {@link CollectionState}. */
@Dao
interface CollectionStateDao extends Serializable {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(CollectionState collectionState);

  @Delete
  void delete(CollectionState collectionState);

  @Query("DELETE FROM collectionState")
  void deleteAll();

  @Query("SELECT * FROM collectionState WHERE id = :id")
  LiveData<CollectionState> findById(String id);

  @Query("SELECT * from collectionState ORDER BY id ASC")
  LiveData<List<CollectionState>> getAll();
}
