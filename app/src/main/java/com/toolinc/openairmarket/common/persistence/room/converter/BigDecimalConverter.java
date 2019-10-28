package com.toolinc.openairmarket.common.persistence.room.converter;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.text.NumberFormat;

/** Converts back and forth between {@link BigDecimal} and long data types. */
public class BigDecimalConverter {

  @TypeConverter
  public static BigDecimal toDate(String amount) {
    return amount == null ? null : new BigDecimal(amount);
  }

  @TypeConverter
  public static String toTimestamp(BigDecimal amount) {
    return amount == null ? null : NumberFormat.getCurrencyInstance().format(amount);
  }
}
