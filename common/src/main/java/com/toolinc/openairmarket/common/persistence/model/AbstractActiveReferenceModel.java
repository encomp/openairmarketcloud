package com.toolinc.openairmarket.common.persistence.model;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.toolinc.openairmarket.common.model.ActiveReferenceModel;

/** Specifies the behavior of the entities that required an alternate primary key. */
public abstract class AbstractActiveReferenceModel extends AbstractActiveModel
    implements ActiveReferenceModel {

  private String referenceId;

  @Override
  public String getReferenceId() {
    return referenceId;
  }

  @Override
  public void setReferenceId(String referenceId) {
    Preconditions.checkState(!Strings.isNullOrEmpty(referenceId));
    this.referenceId = referenceId;
  }
}
