package com.toolinc.openairmarket.persistence.offline.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/** Provides direct access to the underlying database. */
@Database(
    entities = {CollectionState.class},
    exportSchema = false,
    version = 1)
abstract class OfflineRoomDatabase extends RoomDatabase {

  public abstract CollectionStateDao collectionStateDao();
}
