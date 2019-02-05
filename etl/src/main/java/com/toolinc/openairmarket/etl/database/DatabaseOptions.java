package com.toolinc.openairmarket.etl.database;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import javax.inject.Singleton;

/** Specifies the command line arguments for the database module. */
@Singleton
public final class DatabaseOptions extends OptionsBase {

  @Option(
      name = "h2Url",
      help = "The jdbc url that will be used to open a connection with H2.",
      category = "startup",
      defaultValue = "jdbc:h2:~/test;MULTI_THREADED=TRUE")
  public String h2Url;

  @Option(
      name = "h2User",
      help = "The user that will be used to open a connection with H2.",
      category = "startup",
      defaultValue = "sa")
  public String h2User;

  @Option(
      name = "h2Pass",
      help = "The password that will be use to open a connection with H2.",
      category = "startup",
      defaultValue = "")
  public String h2Password;

  @Option(
      name = "h2MaxPoolSize",
      help = "Defines the maximum size of the connection pool.",
      category = "startup",
      defaultValue = "1")
  public int h2MaxPoolSize;

  @Option(
      name = "h2FilePath",
      help = "Defines the path of file that contains the environment variables.",
      category = "startup",
      defaultValue = "1")
  public String h2FilePath;

  @Option(
      name = "msSqlUrl",
      help = "The jdbc url that will be used to open a connection with MS-SQL.",
      category = "startup",
      defaultValue = "jdbc:sqlserver://localhost:1433;database=SPVGanaMasSQL")
  public String msSqlUrl;

  @Option(
      name = "msSqlUser",
      help = "The user that will be used to open a connection with MS-SQL.",
      category = "startup",
      defaultValue = "root")
  public String msSqlUser;

  @Option(
      name = "msSqlPass",
      help = "The password that will be use to open a connection with MS-SQL.",
      category = "startup",
      defaultValue = "toor")
  public String msSqlPass;

  @Option(
      name = "msSqlMaxPoolSize",
      help = "Defines the maximum size of the connection pool.",
      category = "startup",
      defaultValue = "1")
  public int msSqlMaxPoolSize;
}
