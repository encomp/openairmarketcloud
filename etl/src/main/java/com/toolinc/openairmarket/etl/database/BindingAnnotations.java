package com.toolinc.openairmarket.etl.database;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Binding annotations for configuration objects. */
public class BindingAnnotations {

  /** Specifies the binding for H2 database. */
  public static class H2 {

    /** A {@link BindingAnnotation} for injecting the h2 {@code javax.sql.DataSource}. */
    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface DataSource {}

    /** A {@link BindingAnnotation} for injecting the h2 environment variables. */
    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface EnvironmentVariables {}
  }

  /** Specifies the binding for spv gana mas database. */
  public static class MsSql {

    /** A {@link BindingAnnotation} for injecting the spv gana mas {@code javax.sql.DataSource}. */
    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface DataSource {}
  }

  /**
   * A {@link com.google.inject.BindingAnnotation} for injecting the specified char set for the
   * {@code com.toolinc.openairmarket.etl.database.MssqlExtractService}.
   */
  @BindingAnnotation
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
  public @interface CharSet {}

  /**
   * A {@link com.google.inject.BindingAnnotation} for injecting a the {@code
   * com.google.corp.workday.common.services.files.CsvFile.CsvConfiguration} for the {@code
   * com.toolinc.openairmarket.etl.database.MssqlExtractService}.
   */
  @BindingAnnotation
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
  public @interface CsvWriter {}
}
