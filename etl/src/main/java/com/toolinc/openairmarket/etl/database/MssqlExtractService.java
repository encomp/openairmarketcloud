package com.toolinc.openairmarket.etl.database;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.toolinc.openairmarket.etl.database.BindingAnnotations.CharSet;
import static com.toolinc.openairmarket.etl.database.BindingAnnotations.CsvWriter;
import static com.toolinc.openairmarket.etl.database.BindingAnnotations.MsSql;

import com.google.common.flogger.FluentLogger;
import com.toolinc.openairmarket.etl.ExtractService;
import com.toolinc.openairmarket.etl.file.CsvFile;
import com.toolinc.openairmarket.etl.file.CsvFile.CsvFileConfiguration;
import com.toolinc.openairmarket.etl.file.CsvFileWritter;
import com.toolinc.openairmarket.etl.file.SqlScriptReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Inject;
import javax.sql.DataSource;

/** Extract information from ghr database and stores the extracted information in a csv file. */
public final class MssqlExtractService implements ExtractService {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String SELECT = "SELECT";
  private final DataSource dataSource;
  private final CsvFile.CsvConfiguration csvConfiguration;
  private final String charset;
  private final SqlScriptReader sqlScriptReader;
  private Connection connection;

  @Inject
  public MssqlExtractService(
      @MsSql.DataSource DataSource dataSource,
      @CsvWriter CsvFile.CsvConfiguration csvConfiguration,
      @CharSet String charset,
      SqlScriptReader sqlScriptReader) {
    this.dataSource = checkNotNull(dataSource);
    this.csvConfiguration = checkNotNull(csvConfiguration);
    this.charset = checkNotNull(charset);
    this.sqlScriptReader = checkNotNull(sqlScriptReader);
  }

  @Override
  public boolean extract(String scriptPath, String outputPath, String fileName) {
    String[] scripts = readSqlStatements(scriptPath);
    for (String script : scripts) {
      SQLType type = resolveStatementType(script);
      switch (type) {
        case UPDATE:
          executeUpdate(script);
          break;

        case SELECT:
          executeQuery(script, outputPath, fileName);
          break;

        default:
          throw new IllegalStateException(
              "Unexpected behavior could not resolve the appropriate SQL.");
      }
    }
    close(connection);
    return true;
  }

  /**
   * Read the SQL statements contained in file.
   *
   * @param scriptPath the file from which the statements will be retrieved.
   * @return a set of SQL statements.
   */
  private String[] readSqlStatements(String scriptPath) {
    String msg = String.format("The script file [%s] is empty.", scriptPath);
    String[] scripts = checkNotNull(sqlScriptReader.readSqlStatements(scriptPath), msg);
    checkState(scripts.length > 0, msg);
    return scripts;
  }

  /** Specifies the type of sql statements. */
  private enum SQLType {
    @SuppressWarnings("hiding")
    SELECT,
    UPDATE
  }

  /**
   * Determines the type of statement contained in a string.
   *
   * @param statement the statement that needs to be resolved
   * @return the type of statement.
   */
  private SQLType resolveStatementType(String statement) {
    String tmpScript = statement.toUpperCase().trim();
    if (tmpScript.startsWith(SELECT)) {
      return SQLType.SELECT;
    }
    return SQLType.UPDATE;
  }

  /**
   * Executes the given SQL statement, which may be an <code>INSERT</code>, <code>UPDATE</code>, or
   * <code>DELETE</code> statement or an SQL statement that returns nothing, such as an SQL DDL
   * statement.
   *
   * @param sql the statement to execute
   */
  private void executeUpdate(String sql) {
    try (Statement statement = getConnection().createStatement()) {
      logger.atFiner().log(String.format("Executing the script [%s].", sql));
      int rows = statement.executeUpdate(sql);
      logger.atInfo().log(String.format("Affected rows [%d].", rows));
    } catch (SQLException exc) {
      String message = String.format("An error occurred while executing the sql [%s].", sql);
      logger.atSevere().log(message, exc);
      throw new IllegalStateException(message, exc);
    }
  }

  /**
   * Extracts the information from the database and stores it in a csv file.
   *
   * @param sql the SQL <code>SELECT</code> statement that will be executed
   * @param outputPath the path in which the new csv file will be created
   * @param fileName the name of the csv file
   */
  private void executeQuery(String sql, String outputPath, String fileName) {
    try (Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        CsvFileWritter csvFile = createCsvFile(outputPath, fileName)) {
      csvFile.open();
      csvFile.write(resultSet, true);
    } catch (SQLException | IOException exc) {
      String message = String.format("An error occurred while executing the sql [%s].", sql);
      logger.atSevere().log(message, exc);
      throw new IllegalStateException(message, exc);
    }
  }

  private CsvFileWritter createCsvFile(String path, String fileName) {
    return new CsvFileWritter(csvConfiguration, createFileConfiguration(path, fileName));
  }

  private CsvFileConfiguration createFileConfiguration(String path, String fileName) {
    return CsvFileConfiguration.create(path, fileName, charset, null);
  }

  private Connection getConnection() {
    if (connection == null) {
      synchronized (MssqlExtractService.class) {
        if (connection == null) {
          try {
            this.connection = checkNotNull(dataSource.getConnection());
          } catch (SQLException e) {
            throw new IllegalStateException(
                String.format("Unable to acquire a connection [%s].", e.getMessage()), e);
          }
        }
      }
    }
    return connection;
  }

  private void close(AutoCloseable statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (Exception exc) {
        logger.atWarning().log("An error occurred while closing the statement.", exc);
      }
    }
  }
}
