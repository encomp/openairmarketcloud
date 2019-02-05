package com.toolinc.openairmarket.common.model.audit;

import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.model.security.User;

import java.time.LocalDateTime;

/**
 * Specifies the contract for a revision entity.
 *
 * @param <T> specifies the {@code User}.
 */
public interface AuditModel<T extends User> extends Domain {

  LocalDateTime getCreatedDate();

  public void setCreatedDate(LocalDateTime createdDate);

  public T getUser();

  public void setUser(T user);

  public AuditType getAuditType();

  public void setAuditType(AuditType auditType);
}
