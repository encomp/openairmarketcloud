package com.toolinc.openairmarket.common.persistence.room.model;

import androidx.room.ColumnInfo;

import com.google.auto.value.AutoValue;
import com.toolinc.openairmarket.common.model.CatalogModel;

/**
 * Specifies the behavior of the entities that are catalogs. This model is designed to be use with
 * {@link com.google.auto.value.AutoValue}.
 */
public abstract class AbstractRoomCatalogModel extends AbstractRoomActiveReferenceModel {

  @AutoValue.CopyAnnotations
  @ColumnInfo(name = "name")
  public abstract String name();
}
