package com.toolinc.openairmarket.common.persistence.model.audit;

import com.google.common.base.Preconditions;
import com.toolinc.openairmarket.common.persistence.model.AbstractActiveModel;

/**
 * Specifies the behavior of the auditable of the entities ({@code HistoryTenant}) that are required
 * to keep tenancy.
 */
public abstract class AbstractAuditActiveModel extends AbstractActiveModel
    implements AuditActiveModel {

  private Auditable auditable;

  @Override
  public Auditable getAuditable() {
    return auditable;
  }

  @Override
  public void setAuditable(Auditable auditable) {
    this.auditable = Preconditions.checkNotNull(auditable);
  }
}
