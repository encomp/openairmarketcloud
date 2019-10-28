package com.toolinc.openairmarket.persistence.local.offline;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.toolinc.openairmarket.common.persistence.room.converter.DateTimeConverter;
import com.toolinc.openairmarket.persistence.local.offline.converter.StatusConverter;

/** Provides direct access to the underlying database. */
@Database(
    entities = {CollectionSyncState.class},
    exportSchema = false,
    version = 1)
@TypeConverters({DateTimeConverter.class, StatusConverter.class})
abstract class OfflineRoomDatabase extends RoomDatabase {

  public abstract CollectionSyncStateDao collectionStateDao();
}
