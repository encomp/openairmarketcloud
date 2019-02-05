package com.toolinc.openairmarket.etl.file;

import com.google.common.flogger.FluentLogger;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/** Reads the content of a SQL script file. */
public final class SqlScriptReader {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String NEW_LINE_APPENDER = "\n";

  /**
   * Reads the content of a script.
   *
   * @param fileName the desired script
   * @return the content of the script
   */
  public String readSqlScript(String fileName) {
    return readContent(fileName, NEW_LINE_APPENDER);
  }

  /**
   * Provides a set of statements that a script contains.
   *
   * @param fileName the desired script
   * @return a set of the statements found in the script
   */
  public String[] readSqlStatements(String fileName) {
    return readContent(fileName, NEW_LINE_APPENDER).split(";\n");
  }

  private String readContent(String fileName, String appender) {
    logger.atFiner().log(String.format("Reading the content of the file [%s].", fileName));
    StringBuilder scripts = new StringBuilder();
    try (InputStreamReader inputStreamReader =
            new InputStreamReader(new FileInputStream(fileName));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
      String line = bufferedReader.readLine();
      while (line != null) {
        scripts.append(line.trim());
        scripts.append(appender);
        line = bufferedReader.readLine();
      }
    } catch (IOException exc) {
      logger.atSevere().log("Unable to read the content of the file: [%s]", fileName);
      throw new IllegalStateException(exc);
    }
    logger.atFiner().log(String.format("Finish reading the content of the file [%s].", fileName));
    return scripts.toString();
  }
}
