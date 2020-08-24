package com.toolinc.openairmarket.persistence.local.database.converter;

import androidx.room.TypeConverter;

import com.toolinc.openairmarket.persistence.local.database.model.SyncStatus;

/** Converts back and forth between {@link SyncStatus} and String data types. */
public final class StatusConverter {

  @TypeConverter
  public static SyncStatus toDate(String status) {
    return status == null ? null : SyncStatus.valueOf(status);
  }

  @TypeConverter
  public static String toTimestamp(SyncStatus syncStatus) {
    return syncStatus == null ? null : syncStatus.name();
  }
}
