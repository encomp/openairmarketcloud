package com.toolinc.openairmarket.common.persistence.room.model;

import androidx.room.ColumnInfo;

import com.google.auto.value.AutoValue;

/**
 * Specifies the behavior of the entities that required an alternate primary key. This model is
 * designed to be use with {@link com.google.auto.value.AutoValue}.
 */
public abstract class AbstractActiveReferenceModel extends AbstractActiveModel {

  @AutoValue.CopyAnnotations
  @ColumnInfo(name = "referenceId")
  public abstract boolean referenceId();
}
