package com.toolinc.openairmarket.etl.database;

import com.google.common.base.Preconditions;
import com.google.common.flogger.FluentLogger;
import com.toolinc.openairmarket.etl.ConversionService;
import com.toolinc.openairmarket.etl.file.SqlScriptReader;
import javax.inject.Inject;
import javax.inject.Singleton;

/** Performs data transformation in h2 database based on a set of SQL scripts. */
@Singleton
final class H2ConversionService implements ConversionService {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final H2DataBaseHelper databaseHelper;
  private final SqlScriptReader sqlScriptReader;

  @Inject
  public H2ConversionService(H2DataBaseHelper databaseHelper, SqlScriptReader sqlScriptReader) {
    this.databaseHelper = Preconditions.checkNotNull(databaseHelper);
    this.sqlScriptReader = Preconditions.checkNotNull(sqlScriptReader);
  }

  @Override
  public boolean transform(String... fileNames) {
    logger.atFiner().log("About to execute the following scripts [%s].", fileNames);
    Preconditions.checkNotNull(fileNames, "The file names are null.");
    for (String fileName : fileNames) {
      logger.atFiner().log("Executing the following script [%s].", fileName);
      databaseHelper.executeUpdate(sqlScriptReader.readSqlScript(fileName));
    }
    return true;
  }
}
