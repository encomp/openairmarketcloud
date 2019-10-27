package com.toolinc.openairmarket.common.persistence.room.model;

import androidx.room.ColumnInfo;

import com.google.auto.value.AutoValue;

/**
 * Specifies the behavior of the entities that are catalogs. This model is designed to be use with
 * {@link com.google.auto.value.AutoValue}.
 */
public abstract class AbstractCatalogModel {

  @AutoValue.CopyAnnotations
  @ColumnInfo(name = "name")
  public abstract boolean name();
}
