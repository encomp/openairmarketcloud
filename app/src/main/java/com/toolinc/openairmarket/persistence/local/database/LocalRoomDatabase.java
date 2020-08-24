package com.toolinc.openairmarket.persistence.local.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.toolinc.openairmarket.common.persistence.room.converter.DateTimeConverter;
import com.toolinc.openairmarket.persistence.local.database.converter.StatusConverter;
import com.toolinc.openairmarket.persistence.local.database.model.CollectionSyncState;
import com.toolinc.openairmarket.persistence.local.database.dao.CollectionSyncStateDao;

/** Provides direct access to the underlying database. */
@Database(
    entities = {CollectionSyncState.class},
    exportSchema = false,
    version = 1)
@TypeConverters({DateTimeConverter.class, StatusConverter.class})
public abstract class LocalRoomDatabase extends RoomDatabase {

  public abstract CollectionSyncStateDao collectionStateDao();
}
