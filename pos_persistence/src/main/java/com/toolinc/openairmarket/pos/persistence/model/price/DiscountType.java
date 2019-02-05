package com.toolinc.openairmarket.pos.persistence.model.price;

/** Defines the different types of discount types. */
public enum DiscountType {

  /** Discount based on a break. */
  BREAKS,

  /** Discount based on a percentage. */
  FLAT_PERCENT,

  /** Discount based on a price list. */
  PRICE_LIST,

  /** Discount based on a particular formula. */
  FORMULA
}
