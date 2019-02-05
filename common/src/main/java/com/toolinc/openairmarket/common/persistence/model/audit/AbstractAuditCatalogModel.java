package com.toolinc.openairmarket.common.persistence.model.audit;

import com.toolinc.openairmarket.common.model.CatalogModel;

/** Specifies the behavior of the audit of the entities ({@code CatalogModel}). */
public abstract class AbstractAuditCatalogModel extends AbstractAuditActiveReferenceModel
    implements CatalogModel {

  private String name;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = checkNotEmpty(name);
  }
}
