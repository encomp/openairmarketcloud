package com.toolinc.openairmarket.common.room.converter;

import androidx.room.TypeConverter;

import org.joda.time.DateTime;

/** Converts back and forth between {@link DateTime} and long data types. */
public final class DateTimeConverter {

  @TypeConverter
  public static DateTime toDate(Long timestamp) {
    return timestamp == null ? null : new DateTime(timestamp);
  }

  @TypeConverter
  public static Long toTimestamp(DateTime dateTime) {
    return dateTime == null ? null : dateTime.getMillis();
  }
}
