package com.toolinc.openairmarket.common.persistence.room.model;

import androidx.room.ColumnInfo;

import com.google.auto.value.AutoValue;
import com.toolinc.openairmarket.common.persistence.model.AbstractModel;

/**
 * Specifies the behavior of the entities that need to keep the active or inactive state. This model
 * is designed to be use with {@link com.google.auto.value.AutoValue}.
 */
public abstract class AbstractRoomActiveModel extends AbstractModel {

  /** Specifies the current state of an entity. */
  @AutoValue.CopyAnnotations
  @ColumnInfo(name = "active")
  public abstract boolean active();
}
