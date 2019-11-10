package com.toolinc.openairmarket.common.persistence.room.converter;

import androidx.room.TypeConverter;

import com.toolinc.openairmarket.pos.persistence.model.product.ProductType;

/** Converts back and forth between {@link ProductType} and String types. */
public class ProductTypeConverter {

  @TypeConverter
  public static ProductType toProductType(String value) {
    return ProductType.valueOf(value);
  }

  @TypeConverter
  public static String toString(ProductType productType) {
    return productType == null ? null : productType.name();
  }
}
