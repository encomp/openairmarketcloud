package com.toolinc.openairmarket.persistence.local.pos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import com.google.auto.value.AutoValue;
import com.google.auto.value.AutoValue.CopyAnnotations;

import java.math.BigDecimal;

/** Specifies the price of a product price of how it should be bought. */
@AutoValue
public abstract class ProductRoomPurchasePrice {

  /** The official net price of a product in a specified currency. */
  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "purchaseListPrice")
  public abstract BigDecimal listPrice();

  /** The regular or normal price of a product in the respective price list. */
  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "purchasePrice")
  public abstract BigDecimal price();

  /** The lowest net price a specified item may be bought for. */
  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "purchaseLimitPrice")
  public abstract BigDecimal limitPrice();

  public static ProductRoomPurchasePrice create(
      BigDecimal listPrice, BigDecimal price, BigDecimal limitPrice) {
    return new AutoValue_ProductRoomPurchasePrice(listPrice, price, limitPrice);
  }
}
