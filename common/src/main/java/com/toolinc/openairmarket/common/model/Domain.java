package com.toolinc.openairmarket.common.model;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/** Marker inteface that specifies that an object is a model. */
public interface Domain extends Serializable {

  static final DecimalFormat MONEY = new DecimalFormat();

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null, as
   * well as is not negative.
   *
   * @param <E> a Number
   * @param value an object reference
   * @return the reference that was validated
   */
  default <E extends Number> E checkPositive(E value) {
    Preconditions.checkNotNull(value);
    Preconditions.checkState(value.doubleValue() > 0.0);
    return value;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null, as
   * well as is not empty.
   *
   * @param value an object reference
   * @return the reference that was validated
   */
  default String checkNotEmpty(String value) {
    Preconditions.checkState(!Strings.isNullOrEmpty(value));
    return value.trim().toUpperCase();
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is nillable, as
   * well as is not empty.
   *
   * @param value an object reference
   * @return the reference that was validated
   */
  default String checkNillable(String value) {
    if (!Strings.isNullOrEmpty(value)) {
      return checkNotEmpty(value);
    }
    return value;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is nillable, as
   * well as is not negative.
   *
   * @param value an object reference
   * @return the reference that was validated
   */
  default <E extends Number> E checkNillablePositive(E value) {
    if (value != null) {
      return checkPositive(value);
    }
    return value;
  }

  /**
   * Converts to {@link String} a given {@link BigDecimal} value.
   *
   * @param bigDecimal the value to format as string
   * @return its string representation
   */
  default String toStringMoney(BigDecimal bigDecimal) {
    if (bigDecimal != null) {
      MONEY.setMinimumFractionDigits(0);
      MONEY.setMaximumFractionDigits(4);
      MONEY.setMaximumIntegerDigits(10);
      MONEY.setMinimumIntegerDigits(0);
      return MONEY.format(bigDecimal);
    }
    return null;
  }
}
