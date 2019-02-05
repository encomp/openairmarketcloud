package com.toolinc.openairmarket.etl.file;

import com.google.common.base.Preconditions;
import com.google.common.flogger.FluentLogger;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.inject.Inject;

/** Defines a csv file writer. */
public final class CsvFileWritter implements CsvFile {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final CsvFileConfiguration csvFileConfiguration;
  private final CsvConfiguration csvConfiguration;
  private CsvWriter csvWriter;

  /**
   * Creates a new instance.
   *
   * @param csvConfig specifies the text delimiter and text separator for a csv file.
   * @param csvFileConfig specifies the directory, name and header of a csv file.
   */
  @Inject
  public CsvFileWritter(CsvConfiguration csvConfig, CsvFileConfiguration csvFileConfig) {
    this.csvFileConfiguration =
        Preconditions.checkNotNull(csvFileConfig, "Csv file configuration is missing.");
    this.csvConfiguration = Preconditions.checkNotNull(csvConfig, "Csv configuration is missing.");
  }

  @Override
  public void open() {
    try {
      BufferedOutputStream outputStream =
          new BufferedOutputStream(new FileOutputStream(getFileName()));
      OutputStreamWriter writer =
          new OutputStreamWriter(outputStream, csvFileConfiguration.charset());
      csvWriter =
          CsvWriter.builder()
              .setWriter(writer)
              .setSeparator(csvConfiguration.textSeparator())
              .setQuoteChar(csvConfiguration.textDelimiter())
              .build();
      logger.atInfo().log(String.format("A csv file was created [%s]", getFileName()));
      writeHeader();
    } catch (IOException ex) {
      String message =
          String.format(
              "Error while writing on file [%s%s]",
              csvFileConfiguration.filePath(), csvFileConfiguration.fileName());
      logger.atSevere().log(message, ex);
      throw new IllegalStateException(message, ex);
    }
  }

  /**
   * Writes the specified array of string as a line.
   *
   * @param line the line that will be written
   */
  public void write(String[] line) {
    csvWriter.writeNext(line);
  }

  /**
   * Writes the specified {@code ResultSet} in the csv file.
   *
   * @param resultSet the {@code ResultSet} that will be written in the file.
   * @param flag specifies whether the headers of the {@code ResultSet} will be written.
   * @throws IOException Indicates that an I/O exception of some sort has occurred.
   * @throws java.sql.SQLException Indicates that a database exception of some sort has occurred.
   */
  public void write(ResultSet resultSet, boolean flag) throws IOException, SQLException {
    csvWriter.writeAll(resultSet, flag);
  }

  private void writeHeader() {
    if (csvFileConfiguration.header().isPresent()) {
      logger.atInfo().log(
          String.format(
              "Writing the headers of the file [%s].", csvFileConfiguration.header().get()));
      String[] headers =
          csvFileConfiguration.header().get().split("" + csvConfiguration.textSeparator());
      write(headers);
    }
  }

  @Override
  public void close() {
    if (csvWriter != null) {
      csvWriter.close();
    }
  }

  private String getFileName() {
    return csvFileConfiguration.filePath() + csvFileConfiguration.fileName();
  }
}
