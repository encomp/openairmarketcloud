package com.toolinc.openairmarket.common.persistence.model;

import com.toolinc.openairmarket.common.model.CatalogModel;

/** Specifies the behavior of the entities that are catalogs. */
public abstract class AbstractCatalogModel extends AbstractActiveReferenceModel
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
