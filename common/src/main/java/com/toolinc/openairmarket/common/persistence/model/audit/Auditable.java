package com.toolinc.openairmarket.common.persistence.model.audit;

import com.google.common.base.Preconditions;
import com.toolinc.openairmarket.common.model.audit.AuditModel;
import com.toolinc.openairmarket.common.model.audit.AuditType;
import com.toolinc.openairmarket.common.persistence.model.security.SystemUser;

import java.time.LocalDateTime;

/**
 * Specifies the revision for an entity that is required to keep an audit.
 */
public class Auditable implements AuditModel<SystemUser> {

    private AuditType auditType;

    private LocalDateTime createdDate;

    private SystemUser user;

    @Override
    public AuditType getAuditType() {
        return auditType;
    }

    @Override
    public void setAuditType(AuditType auditType) {
        this.auditType = Preconditions.checkNotNull(auditType);
    }

    @Override
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = Preconditions.checkNotNull(createdDate);
    }

    @Override
    public SystemUser getUser() {
        return user;
    }

    @Override
    public void setUser(SystemUser systemUser) {
        this.user = Preconditions.checkNotNull(systemUser);
    }
}