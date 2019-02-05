package com.toolinc.openairmarket.etl;

import com.google.common.collect.ImmutableMap;
import com.google.common.flogger.FluentLogger;
import com.google.devtools.common.options.OptionsParser;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.toolinc.openairmarket.etl.database.DatabaseOptions;
import com.toolinc.openairmarket.etl.database.DatabaseServicesModule;
import com.toolinc.openairmarket.etl.database.JdbcDataSourceConfiguration;
import com.toolinc.openairmarket.etl.pipeline.inject.PipelineModule;
import com.toolinc.openairmarket.etl.pipeline.inject.PipelineOptions;
import com.toolinc.openairmarket.etl.pipeline.runner.ExtractPipelineRunner;
import com.toolinc.openairmarket.etl.pipeline.runner.PipelineRunner;
import com.toolinc.openairmarket.etl.pipeline.runner.PipelineRunnerException;
import com.toolinc.openairmarket.etl.pipeline.runner.PlainPipelineRunner;
import java.util.Map;
import java.util.UUID;
import org.apache.log4j.MDC;

/** Pipeline execution. */
public final class Pipeline {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String H2_DRIVER = "org.h2.Driver";
  private static final String MSSQL_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  private static final Map<String, Class> RUNNER =
      ImmutableMap.of("extract", ExtractPipelineRunner.class, "default", PlainPipelineRunner.class);

  public static final void main(String[] args) {
    OptionsParser parser =
        OptionsParser.newOptionsParser(PipelineOptions.class, DatabaseOptions.class);
    parser.parseAndExitUponError(args);
    PipelineOptions pipelineOptions = parser.getOptions(PipelineOptions.class);
    DatabaseOptions dbOptions = parser.getOptions(DatabaseOptions.class);
    UUID uuid = UUID.randomUUID();
    MDC.put("uuid", uuid.toString());
    MDC.put("pipelineId", pipelineOptions.pipelineId);
    // Adding logging information
    logger.atInfo().log("Using the [%s] pipeline runner.", pipelineOptions.pipelineRunner);
    logger.atInfo().log("The following pipeline [%s] will be launch.", pipelineOptions.pipelineId);
    logger.atFiner().log(
        "Using the following configuration file [%s].", pipelineOptions.pipelineConfig);
    logger.atInfo().log(
        "H2 Database configuration:\tURL:%s\tUser:%s\tPass:%s\tMaxPoolSize:%d",
        dbOptions.h2Url, dbOptions.h2User, dbOptions.h2Password, dbOptions.h2MaxPoolSize);
    logger.atInfo().log(
        "MS-SQL Database configuration:\tURL:%s\tUser:%s\tPass:%s\tMaxPoolSize:%d",
        dbOptions.msSqlUrl, dbOptions.msSqlUser, dbOptions.msSqlPass, dbOptions.msSqlMaxPoolSize);
    // Creating the guice injector
    final Injector injector =
        Guice.createInjector(
            new DatabaseServicesModule(
                createH2(dbOptions), createMsSql(dbOptions), dbOptions.h2FilePath),
            new PipelineModule(
                pipelineOptions.pipelineConfig,
                pipelineOptions.scriptsPath,
                pipelineOptions.inputPath,
                pipelineOptions.outputPath));
    @SuppressWarnings("unchecked")
    Class<PipelineRunner> clazz = RUNNER.get(pipelineOptions.pipelineRunner);
    PipelineRunner pipelineRunner = injector.getInstance(clazz);
    try {
      logger.atFiner().log("Start the execution of the pipeline [%s]", pipelineOptions.pipelineId);
      pipelineRunner.execute(pipelineOptions.pipelineId);
      logger.atFiner().log(
          "Complete the execution of the pipeline [%s]", pipelineOptions.pipelineId);
    } catch (PipelineRunnerException exc) {
      logger.atSevere().log("Failed to run the pipeline [%s].", pipelineOptions.pipelineId);
      exc.printStackTrace();
    }
    MDC.remove("uuid");
    MDC.remove("pipelineId");
    System.exit(1);
  }

  private static final JdbcDataSourceConfiguration createH2(DatabaseOptions dbOptions) {
    return JdbcDataSourceConfiguration.builder()
        .setDriver(H2_DRIVER)
        .setUrl(dbOptions.h2Url)
        .setUser(dbOptions.h2User)
        .setPassword(dbOptions.h2Password)
        .setMaxPoolSize(dbOptions.h2MaxPoolSize)
        .build();
  }

  private static final JdbcDataSourceConfiguration createMsSql(DatabaseOptions dbOptions) {
    return JdbcDataSourceConfiguration.builder()
        .setDriver(MSSQL_DRIVER)
        .setUrl(dbOptions.msSqlUrl)
        .setUser(dbOptions.msSqlUser)
        .setPassword(dbOptions.msSqlPass)
        .setMaxPoolSize(dbOptions.msSqlMaxPoolSize)
        .build();
  }
}
