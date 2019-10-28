package com.toolinc.openairmarket.common.persistence.room.converter;

import androidx.room.TypeConverter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/** Converts back and forth between {@link DateTime} and long data types. */
public final class DateTimeConverter {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

  @TypeConverter
  public static DateTime toDate(Long timestamp) {
    return timestamp == null ? null : new DateTime(timestamp);
  }

  @TypeConverter
  public static Long toTimestamp(DateTime dateTime) {
    return dateTime == null ? null : dateTime.getMillis();
  }

  public static String toString(DateTime dateTime) {
    return DATE_TIME_FORMATTER.print(dateTime);
  }
}
