package com.toolinc.openairmarket.common.model;

/**
 * Specifies the behavior for the entities that are catalogs that requires an alternate unique key
 * {@link ActiveReferenceModel}.
 */
public interface CatalogModel extends ActiveReferenceModel {

  /**
   * Provides the description for the unique key of this entity on the database.
   *
   * @return the description assigned
   */
  String getName();

  /**
   * Specifies the description for the unique key of this entity on the database.
   *
   * @param name the description that will be assigned
   */
  void setName(String name);
}
