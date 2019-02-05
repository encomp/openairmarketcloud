package com.toolinc.openairmarket.etl.database;

import com.google.auto.value.AutoValue;
import com.toolinc.openairmarket.etl.database.AutoValue_JdbcDataSourceConfiguration.Builder;

/** Specifies the database configuration for jdbc connections. */
@AutoValue
public abstract class JdbcDataSourceConfiguration {

  public abstract String driver();

  public abstract String url();

  public abstract String user();

  public abstract String password();

  public abstract int maxPoolSize();

  public static final Builder builder() {
    return new AutoValue_JdbcDataSourceConfiguration.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setDriver(String driver);

    public abstract Builder setUrl(String url);

    public abstract Builder setUser(String user);

    public abstract Builder setPassword(String password);

    public abstract Builder setMaxPoolSize(int maxPoolSize);

    public abstract JdbcDataSourceConfiguration build();
  }
}
