package com.toolinc.openairmarket.common.persistence.model;

import com.google.common.base.Preconditions;
import com.toolinc.openairmarket.common.model.ActiveModel;

/** Specifies the behavior of the entities that need to keep the active or inactive state. */
public abstract class AbstractActiveModel extends AbstractModel implements ActiveModel {

  private Boolean active = Boolean.TRUE;

  @Override
  public Boolean getActive() {
    return active;
  }

  @Override
  public void setActive(Boolean active) {
    this.active = Preconditions.checkNotNull(active);
  }
}
