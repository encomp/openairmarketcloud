package com.toolinc.openairmarket.etl.file;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.auto.value.AutoValue;
import com.google.common.flogger.FluentLogger;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

/** This File is used as Helper Class for WriteOperations in CSV files. */
@AutoValue
public abstract class CsvWriter implements Closeable {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final char NO_QUOTE_CHARACTER = '\u0000';
  private static final char NO_ESCAPE_CHARACTER = '\u0000';

  abstract PrintWriter printWriter();

  abstract char separator();

  abstract char quoteChar();

  abstract char escapeChar();

  abstract String lineEnd();

  abstract String csvDefaultValue();

  abstract SimpleDateFormat timestampFormatter();

  abstract SimpleDateFormat dateFormatter();

  /**
   * Writes the entire list to a CSV file. The list is assumed to be a String[]
   *
   * @param allLines a List of String[], with each String[] representing a line of the file.
   */
  public void writeAll(List<String[]> allLines) {
    Iterator<String[]> iter = allLines.iterator();
    while (iter.hasNext()) {
      String[] nextLine = iter.next();
      writeNext(nextLine);
    }
  }

  /**
   * Writes the entire ResultSet to a CSV file.
   *
   * <p>The caller is responsible for closing the ResultSet.
   *
   * @param rs the recordset to write
   * @param includeColumnNames true if you want column names in the output, false otherwise
   */
  public void writeAll(ResultSet rs, boolean includeColumnNames) throws SQLException, IOException {
    ResultSetMetaData metadata = rs.getMetaData();
    if (includeColumnNames) {
      writeColumnNames(metadata);
    }
    int columnCount = metadata.getColumnCount();
    int row = 1;
    while (rs.next()) {
      String[] nextLine = new String[columnCount];
      for (int i = 0; i < columnCount; i++) {
        RetryableWrite retryableWrite = RetryableWrite.create(CsvWriter.this, row, i, rs, metadata);
        nextLine[i] = retryableWrite.write();
      }
      writeNext(nextLine);
      row++;
    }
  }

  private void writeColumnNames(ResultSetMetaData metadata) throws SQLException {
    int columnCount = metadata.getColumnCount();
    String[] nextLine = new String[columnCount];
    for (int i = 0; i < columnCount; i++) {
      nextLine[i] = metadata.getColumnLabel(i + 1);
    }
    writeNext(nextLine);
  }

  /**
   * Writes the next line to the file.
   *
   * @param nextLine a string array with each comma-separated element as a separate entry.
   */
  public void writeNext(String[] nextLine) {
    if (nextLine == null) {
      return;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < nextLine.length; i++) {
      if (i != 0) {
        sb.append(separator());
      }
      String nextElement = nextLine[i];

      if (nextElement == null) {
        continue;
      }
      if (quoteChar() != NO_QUOTE_CHARACTER) {
        sb.append(quoteChar());
        for (int j = 0; j < nextElement.length(); j++) {
          char nextChar = nextElement.charAt(j);
          if (escapeChar() != NO_ESCAPE_CHARACTER && nextChar == quoteChar()) {
            sb.append(escapeChar()).append(nextChar);
          } else if (escapeChar() != NO_ESCAPE_CHARACTER && nextChar == escapeChar()) {
            sb.append(escapeChar()).append(nextChar);
          } else {
            sb.append(nextChar);
          }
        }
      }
      if (quoteChar() != NO_QUOTE_CHARACTER) {
        sb.append(quoteChar());
      }
    }
    sb.append(lineEnd());
    printWriter().write(sb.toString());
  }

  /** Flush underlying stream to writer. */
  public void flush() {
    printWriter().flush();
  }

  /** Close the underlying stream writer flushing any buffered content. */
  @Override
  public void close() {
    flush();
    printWriter().close();
  }

  /**
   * Creates a new instance of {@link Builder} with the default values.
   *
   * @return {@link Builder}.
   */
  public static Builder builder() {
    return new AutoValue_CsvWriter.Builder()
        .setEscapeChar(Builder.ESCAPE_CHAR)
        .setSeparator(Builder.SEPARATOR)
        .setQuoteChar(Builder.QUOTE_CHAR)
        .setLineEnd(Builder.END_LINE)
        .setCsvDefaultValue(Builder.DEFAULT_VALUE)
        .setTimestampFormatter(Builder.TIMESTAMP_FORMAT)
        .setDateFormatter(Builder.DATE_FORMAT);
  }

  /**
   * Creates a new instance of ${@link Builder} from the current instance.
   *
   * @return @return {@link Builder}.
   */
  public abstract Builder toBuilder();

  /** Constructs {@code CsvWriter} using a comma as a default separator. */
  @AutoValue.Builder
  public abstract static class Builder {

    private static final char ESCAPE_CHAR = '"';
    private static final char SEPARATOR = ',';
    private static final char QUOTE_CHAR = '"';
    private static final String END_LINE = "\n";
    private static final String DEFAULT_VALUE = "";
    private static final SimpleDateFormat TIMESTAMP_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    abstract PrintWriter printWriter();

    public Builder setWriter(Writer writer) {
      return setPrintWriter(new PrintWriter(checkNotNull(writer)));
    }

    public abstract Builder setPrintWriter(PrintWriter printWriter);

    public abstract Builder setEscapeChar(char val);

    public abstract Builder setSeparator(char val);

    public abstract Builder setQuoteChar(char val);

    public abstract Builder setLineEnd(String val);

    public abstract Builder setCsvDefaultValue(String val);

    public abstract Builder setTimestampFormatter(SimpleDateFormat timestampFormatter);

    public abstract Builder setDateFormatter(SimpleDateFormat dateFormatter);

    public abstract CsvWriter build();
  }
}
