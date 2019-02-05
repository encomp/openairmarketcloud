package com.toolinc.openairmarket.common.model;

/** Specifies the behavior of the entities that need to keep the active or inactive state. */
public interface ActiveModel extends Model {

  /**
   * Returns the current state of an entity.
   *
   * @return true is the entity is active or false for an inactive entity.
   */
  Boolean getActive();

  /**
   * Specifies the state of an entity.
   *
   * @param active if the value being pass is true the entity will be active otherwise will be
   *     inactive
   */
  void setActive(Boolean active);
}
