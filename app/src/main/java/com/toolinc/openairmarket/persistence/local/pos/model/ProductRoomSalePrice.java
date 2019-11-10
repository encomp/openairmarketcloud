package com.toolinc.openairmarket.persistence.local.pos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import com.google.auto.value.AutoValue;
import com.google.auto.value.AutoValue.CopyAnnotations;

import java.math.BigDecimal;

/** Specifies the price of a product the price of how it should be sold. */
@AutoValue
public abstract class ProductRoomSalePrice {

  /** The official net price of a product in a specified currency. */
  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "saleListPrice")
  public abstract BigDecimal listPrice();

  /** The regular or normal price of a product in the respective price list. */
  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "salePrice")
  public abstract BigDecimal price();

  /** The lowest net price a specified item may be sold for. */
  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "saleLimitPrice")
  public abstract BigDecimal limitPrice();

  /** The profit that will be made. */
  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "saleProfit")
  public abstract BigDecimal profit();

  public static ProductRoomSalePrice create(
      BigDecimal listPrice, BigDecimal price, BigDecimal limitPrice, BigDecimal profit) {
    return new AutoValue_ProductRoomSalePrice(listPrice, price, limitPrice, profit);
  }
}
