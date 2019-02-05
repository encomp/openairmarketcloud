package com.toolinc.openairmarket.pos.persistence.model.product;

/** Specifies the type of {@code Product}. */
public enum ProductType {

  /**
   * Inventory held for resale, materials that are placed into a production process, and
   * semi-finished or finished goods created through production are examples of products defined
   * using the product type "Item".
   */
  ITEM,

  /**
   * This product type is used to identify such provisions as professional services, transportation,
   * telephony, and other items which do not correspond with material goods.
   */
  SERVICE,

  /**
   * Resource type can be used to configure resources such as Financial, Legal or Natural resources
   * used by the organization.
   */
  RESOURCE,

  /**
   * Expense type can be used to configure expenses such as travel expenses to be used while
   * reporting Employee expenses.
   */
  EXPENSE
}
