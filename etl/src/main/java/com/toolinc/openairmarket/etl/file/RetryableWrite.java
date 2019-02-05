package com.toolinc.openairmarket.etl.file;

import com.google.api.client.util.BackOff;
import com.google.api.client.util.BackOffUtils;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.client.util.Sleeper;
import com.google.auto.value.AutoValue;
import com.google.common.flogger.FluentLogger;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;

@AutoValue
abstract class RetryableWrite {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String ERROR_MSG = "BackOff has been exhausted.";
  private static final int WRITE_ATTEMPTS = 3;
  private static final String DECIMAL_ZERO = "0";
  private static final String DECIMAL_POINT = ".";
  private static final String REGEX_EXP_DECIMAL_POINT = "\\.";
  private final Sleeper sleeper = Sleeper.DEFAULT;
  private final BackOff backOff =
      new ExponentialBackOff.Builder()
          .setInitialIntervalMillis(1)
          .setMaxElapsedTimeMillis(50)
          .setMaxIntervalMillis(10)
          .setMultiplier(1.5)
          .setRandomizationFactor(0.5)
          .build();

  abstract String csvDefaultValue();

  abstract SimpleDateFormat timestampFormatter();

  abstract SimpleDateFormat dateFormatter();

  abstract int row();

  abstract int col();

  abstract ResultSet resultSet();

  abstract ResultSetMetaData resultSetMetaData();

  static RetryableWrite create(
      CsvWriter csvWriter, int row, int col, ResultSet rs, ResultSetMetaData metaData) {
    return new AutoValue_RetryableWrite(
        csvWriter.csvDefaultValue(),
        csvWriter.timestampFormatter(),
        csvWriter.dateFormatter(),
        row,
        col,
        rs,
        metaData);
  }

  public String write() throws SQLException, IOException {
    return writeAttempts(WRITE_ATTEMPTS);
  }

  private String writeAttempts(int attempt) throws SQLException, IOException {
    if (attempt == -1) {
      throw new IllegalStateException("Exhausted the maximum number of retries.");
    }
    try {
      return getColumnValue(resultSet(), resultSetMetaData().getColumnType(col() + 1), col() + 1);
    } catch (RuntimeException exc) {
      String msg =
          String.format(
              "Unable to retrieve the value of [%s] at row [%d] attempt [%d].",
              resultSetMetaData().getColumnLabel(col() + 1), row(), attempt - WRITE_ATTEMPTS + 1);
      logger.atWarning().log(msg);
      return backOffWriteRetry(attempt - 1);
    }
  }

  private String backOffWriteRetry(int attempt) throws SQLException, IOException {
    try {
      if (BackOffUtils.next(sleeper, backOff)) {
        return writeAttempts(attempt);
      } else {
        throw new IllegalThreadStateException(ERROR_MSG);
      }
    } catch (InterruptedException exc) {
      throw new IllegalThreadStateException(ERROR_MSG);
    }
  }

  private String getColumnValue(ResultSet rs, int colType, int colIndex)
      throws SQLException, IOException {
    String value = null;
    switch (colType) {
      case Types.BIT:
        Object bit = rs.getObject(colIndex);
        if (bit != null) {
          value = String.valueOf(bit);
        } else {
          value = csvDefaultValue();
        }
        break;
      case Types.BOOLEAN:
        boolean b = rs.getBoolean(colIndex);
        if (!rs.wasNull()) {
          value = Boolean.valueOf(b).toString();
        }
        break;
      case Types.CLOB:
        Clob c = rs.getClob(colIndex);
        if (c != null) {
          value = read(c);
        } else {
          value = csvDefaultValue();
        }
        break;
      case Types.DECIMAL:
      case Types.DOUBLE:
      case Types.FLOAT:
      case Types.REAL:
      case Types.NUMERIC:
        String bd = rs.getString(colIndex);
        int scale = rs.getMetaData().getScale(colIndex);
        if (bd != null) {
          String[] scales = bd.split(REGEX_EXP_DECIMAL_POINT);
          value = "" + bd;
          if (scales.length == 1 && scale > 0) {
            value = value.concat(DECIMAL_POINT);
            for (int i = 0; i < scale; i++) {
              value = value.concat(DECIMAL_ZERO);
            }
          } else if (scales.length >= 2) {
            int count = scales[1].length();
            while (count < scale) {
              value = value.concat(DECIMAL_ZERO);
              count++;
            }
          }
        } else {
          value = csvDefaultValue();
        }
        break;
      case Types.BIGINT:
      case Types.INTEGER:
      case Types.TINYINT:
      case Types.SMALLINT:
        value = rs.getString(colIndex);
        if (value == null) {
          value = csvDefaultValue();
        }
        break;
      case Types.JAVA_OBJECT:
        Object obj = rs.getObject(colIndex);
        if (obj != null) {
          value = String.valueOf(obj);
        } else {
          value = csvDefaultValue();
        }
        break;
      case Types.DATE:
        java.sql.Date date = null;
        try {
          date = rs.getDate(colIndex);
        } catch (SQLException exc) {
          logger.atFiner().log(exc.getMessage());
        }
        if (date != null) {
          value = dateFormatter().format(date);
        }
        break;
      case Types.TIME:
        Time t = rs.getTime(colIndex);
        if (t != null) {
          value = t.toString();
        }
        break;
      case Types.TIMESTAMP:
        Timestamp tstamp = rs.getTimestamp(colIndex);
        if (tstamp != null) {
          value = timestampFormatter().format(tstamp);
        }
        break;
      case Types.LONGVARCHAR:
      case Types.VARCHAR:
      case Types.CHAR:
        value = rs.getString(colIndex);
        if (value == null) {
          value = csvDefaultValue();
        }
        break;
      default:
        value = csvDefaultValue();
    }
    return value;
  }

  private static final String read(Clob c) throws SQLException, IOException {
    StringBuilder sb = new StringBuilder((int) c.length());
    Reader r = c.getCharacterStream();
    char[] cbuf = new char[2048];
    int n = 0;
    while ((n = r.read(cbuf, 0, cbuf.length)) != -1) {
      if (n > 0) {
        sb.append(cbuf, 0, n);
      }
    }
    return sb.toString();
  }
}
