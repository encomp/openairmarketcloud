package com.toolinc.openairmarket.common.model;

/** Specifies the behavior for the entities that requires an alternate primary key. */
public interface ActiveReferenceModel extends ActiveModel {

  /**
   * Provides the specified key that identifies uniquely this entity on the database.
   *
   * @return the unique of this entity.
   */
  String getReferenceId();

  /**
   * Specifies the key that identifies uniquely this entity on the database.
   *
   * @param referenceId the unique key.
   */
  void setReferenceId(String referenceId);
}
