package com.toolinc.openairmarket.etl.database;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.isNullOrEmpty;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.toolinc.openairmarket.etl.ConversionService;
import com.toolinc.openairmarket.etl.ExtractService;
import com.toolinc.openairmarket.etl.database.BindingAnnotations.CharSet;
import com.toolinc.openairmarket.etl.database.BindingAnnotations.CsvWriter;
import com.toolinc.openairmarket.etl.database.BindingAnnotations.H2;
import com.toolinc.openairmarket.etl.database.BindingAnnotations.MsSql;
import com.toolinc.openairmarket.etl.file.CsvFile.CsvConfiguration;
import com.toolinc.openairmarket.etl.file.SqlScriptReader;
import java.beans.PropertyVetoException;
import java.util.List;
import javax.inject.Singleton;
import javax.sql.DataSource;

/** Bindings for the database services module. */
public final class DatabaseServicesModule extends AbstractModule {

  private final JdbcDataSourceConfiguration mssqlConfiguration;
  private final JdbcDataSourceConfiguration h2Configuration;
  private final String h2FilePath;

  public DatabaseServicesModule(
      JdbcDataSourceConfiguration h2Configuration,
      JdbcDataSourceConfiguration mssqlConfiguration,
      String h2FilePath) {
    this.h2Configuration = checkNotNull(h2Configuration, "Missing H2 configuration.");
    this.mssqlConfiguration = checkNotNull(mssqlConfiguration, "Missing MS-SQL configuration.");
    this.h2FilePath =
        checkNotNull(h2FilePath, "Missing file path of the environment variables is missing.");
  }

  @Override
  protected void configure() {
    bind(String.class).annotatedWith(CharSet.class).toInstance("UTF-8");
    bind(CsvConfiguration.class)
        .annotatedWith(CsvWriter.class)
        .toInstance(CsvConfiguration.create('"', ';'));

    bind(SqlScriptReader.class).in(Scopes.SINGLETON);
    bind(ExtractService.class).to(MssqlExtractService.class);
    bind(H2DataBaseHelper.class).in(Scopes.SINGLETON);
    bind(ConversionService.class).to(H2ConversionService.class).in(Scopes.SINGLETON);
  }

  @Provides
  @Singleton
  @H2.EnvironmentVariables
  List<String> providesEnvironmentVariables(SqlScriptReader sqlScriptReader) {
    return ImmutableList.copyOf(sqlScriptReader.readSqlStatements(h2FilePath));
  }

  @Provides
  @Singleton
  @H2.DataSource
  DataSource providesH2DataSource() {
    try {
      return createPool(
          h2Configuration.driver(),
          h2Configuration.url(),
          h2Configuration.user(),
          h2Configuration.password(),
          h2Configuration.maxPoolSize());
    } catch (PropertyVetoException exc) {
      throw new IllegalStateException("Unable to create a DataSource for H2 database.", exc);
    }
  }

  @Provides
  @Singleton
  @MsSql.DataSource
  DataSource providesMsSqlDataSource() {
    try {
      return createPool(
          mssqlConfiguration.driver(),
          mssqlConfiguration.url(),
          mssqlConfiguration.user(),
          mssqlConfiguration.password(),
          mssqlConfiguration.maxPoolSize());
    } catch (PropertyVetoException exc) {
      throw new IllegalStateException("Unable to create a DataSource for MS-SQL database.", exc);
    }
  }

  private ComboPooledDataSource createPool(
      String driver, String url, String user, String pass, int poolSize)
      throws PropertyVetoException {
    checkState(!isNullOrEmpty(driver));
    checkState(!isNullOrEmpty(url));
    checkState(!isNullOrEmpty(user));
    checkNotNull(pass);
    ComboPooledDataSource dataSource = new ComboPooledDataSource();
    dataSource.setDriverClass(driver);
    dataSource.setJdbcUrl(url);
    dataSource.setUser(user);
    dataSource.setPassword(pass);
    dataSource.setMinPoolSize(1);
    dataSource.setMaxPoolSize(poolSize);
    dataSource.setAcquireIncrement(1);
    return dataSource;
  }
}
