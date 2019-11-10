package com.toolinc.openairmarket.common.persistence.room.converter;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

import timber.log.Timber;

/** Converts back and forth between {@link BigDecimal} and long data types. */
public class BigDecimalConverter {

  private static final String TAG = BigDecimalConverter.class.getSimpleName();

  @TypeConverter
  public static BigDecimal toBigDecimal(String amount) {
    BigDecimal bigDecimal = null;
    if (amount != null) {
      try {
        Number number = NumberFormat.getCurrencyInstance().parse(amount);
        bigDecimal = BigDecimal.valueOf(number.doubleValue());
      } catch (ParseException exc) {
        Timber.tag(TAG).w("Unable to transform " + amount);
      }
    }
    return bigDecimal;
  }

  @TypeConverter
  public static String toString(BigDecimal amount) {
    return amount == null ? null : NumberFormat.getCurrencyInstance().format(amount);
  }
}
