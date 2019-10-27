package com.toolinc.openairmarket.common.persistence.room.model;

import androidx.room.ColumnInfo;

import com.google.auto.value.AutoValue;

/**
 * Specifies the behavior of the entities that are catalogs. This model is designed to be use with
 * {@link com.google.auto.value.AutoValue}.
 */
public interface RoomCatalogModel extends RoomActiveReferenceModel {

  /** Provides a description for the unique key of this entity on the database. */
  @AutoValue.CopyAnnotations
  @ColumnInfo(name = "name")
  String name();
}
