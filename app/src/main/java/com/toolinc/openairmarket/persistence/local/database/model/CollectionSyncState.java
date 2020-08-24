package com.toolinc.openairmarket.persistence.local.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.joda.time.DateTime;

import java.io.Serializable;

/** Specifies the sync state of a Firestore collection that will be store on SQLite. */
@Entity(tableName = "CollectionSyncState")
public class CollectionSyncState implements Serializable {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  private String id;

  @NonNull
  @ColumnInfo(name = "status")
  private SyncStatus status;

  @NonNull
  @ColumnInfo(name = "lastUpdate")
  private DateTime lastUpdate;

  @ColumnInfo(name = "numberOfDocs")
  private int numberOfDocs;

  @NonNull
  public String getId() {
    return id;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public SyncStatus getStatus() {
    return status;
  }

  public void setStatus(@NonNull SyncStatus status) {
    this.status = status;
  }

  public DateTime getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(@NonNull DateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public int getNumberOfDocs() {
    return numberOfDocs;
  }

  public void setNumberOfDocs(int numberOfDocs) {
    this.numberOfDocs = numberOfDocs;
  }
}
