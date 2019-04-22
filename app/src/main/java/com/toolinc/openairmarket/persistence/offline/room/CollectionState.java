package com.toolinc.openairmarket.persistence.offline.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/** Specifies state of a Firestore collection that will be store on SQLite. */
@Entity(tableName = "collectionState")
public class CollectionState implements Serializable {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  private String id;

  @NonNull
  @ColumnInfo(name = "status")
  private String status;

  @NonNull
  @ColumnInfo(name = "lastUpdate")
  private String lastUpdate;

  @ColumnInfo(name = "numberOfDocs")
  private int numberOfDocs;

  @NonNull
  public String getId() {
    return id;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(@NonNull String status) {
    this.status = status;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(@NonNull String lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public int getNumberOfDocs() {
    return numberOfDocs;
  }

  public void setNumberOfDocs(int numberOfDocs) {
    this.numberOfDocs = numberOfDocs;
  }
}
