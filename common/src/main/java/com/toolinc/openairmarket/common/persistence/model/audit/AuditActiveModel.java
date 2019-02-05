package com.toolinc.openairmarket.common.persistence.model.audit;

import com.toolinc.openairmarket.common.persistence.model.AbstractActiveModel;

/** Specifies the behavior of the audit of the entities ({@code ActiveModel}). */
public interface AuditActiveModel {

  Auditable getAuditable();

  void setAuditable(Auditable auditable);

  /**
   * Specifies the behavior of a builderClass class that creates a new {@link
   * AbstractAuditActiveModel}.
   *
   * @param <E> specifies the {@link AbstractActiveModel}.
   * @param <A> specifies the {@link AbstractAuditActiveModel}.
   */
  abstract class Builder<E extends AbstractActiveModel, A extends AbstractAuditActiveModel> {
    public abstract A build(E entity);
  }
}
