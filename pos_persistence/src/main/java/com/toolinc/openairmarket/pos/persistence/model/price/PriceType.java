package com.toolinc.openairmarket.pos.persistence.model.price;

/** Defines the starting point for the price before any subsequent discount is applied. */
public enum PriceType {

  /** Fixed Price. */
  FIXED,

  /** List Price. */
  LIST,

  /** Standard Price. */
  STANDARD,

  /** Limit Purchase Order Price. */
  LIMIT,
}
