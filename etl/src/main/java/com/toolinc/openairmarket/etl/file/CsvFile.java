package com.toolinc.openairmarket.etl.file;

import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.util.Optional;

/** Defines a csv file. */
public interface CsvFile extends Closeable {

  /** Open the csv file. */
  void open();

  /** Close the csv file. */
  void close();

  /** Specifies the text delimiter and text separator of a csv file. */
  @AutoValue
  abstract class CsvConfiguration {

    public abstract char textDelimiter();

    public abstract char textSeparator();

    public static CsvConfiguration create(char textDelimiter, char textSeparator) {
      Preconditions.checkState(
          textDelimiter != textSeparator, "Text delimiter and separator cannot be the same.");
      return new AutoValue_CsvFile_CsvConfiguration(textDelimiter, textSeparator);
    }
  }

  /**
   * Specifies the properties of a csv file, such as path of the file, file name, charset and the
   * header.
   */
  @AutoValue
  abstract class CsvFileConfiguration {

    public abstract String filePath();

    public abstract String fileName();

    public abstract String charset();

    public abstract Optional<String> header();

    public static CsvFileConfiguration create(
        String filePath, String fileName, String charset, String header) {
      Preconditions.checkNotNull(filePath, "File path is missing.");
      Preconditions.checkState(filePath.trim().length() > 0, "File path cannot be empty.");
      Preconditions.checkNotNull(fileName, "File name is missing.");
      Preconditions.checkState(fileName.trim().length() > 0, "File name cannot be empty.");
      Preconditions.checkNotNull(charset, "Char set is missing.");
      Preconditions.checkState(charset.trim().length() > 0, "Char set cannot be empty.");
      if (Optional.ofNullable(header).isPresent()) {
        Preconditions.checkNotNull(header, "Header is missing.");
        Preconditions.checkState(header.trim().length() > 0, "Header cannot be empty.");
      }
      return new AutoValue_CsvFile_CsvFileConfiguration(
          filePath, fileName, charset, Optional.ofNullable(header));
    }
  }
}
