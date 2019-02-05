package com.toolinc.openairmarket.etl.pipeline.inject;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Binding annotations for configuration objects. */
public final class BindingAnnotations {

  /** Specifies the binding for all the paths for the pipeline. */
  public static class Paths {

    /** A {@link com.google.inject.BindingAnnotation} for injecting the configuration file path. */
    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface PipelineConfig {}

    /**
     * A {@link com.google.inject.BindingAnnotation} for injecting the path in which the sql scripts
     * are being stored.
     */
    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface Scripts {}

    /**
     * A {@link com.google.inject.BindingAnnotation} for injecting the path in which the extracted
     * information from MSSQL will be stored.
     */
    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface Input {}

    /**
     * A {@link com.google.inject.BindingAnnotation} for injecting the path in which the extracted
     * information from h2 will be stored.
     */
    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface Output {}
  }

  /**
   * Specifies the binding for {@link com.toolinc.openairmarket.etl.pipeline.step.StepExecutor} for the
   * pipeline.
   */
  public static class StepExecutors {

    /**
     * A {@link com.google.inject.BindingAnnotation} for injecting the extraction phase executor.
     */
    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface Extraction {}

    /**
     * A {@link com.google.inject.BindingAnnotation} for injecting the transformation phase
     * executor.
     */
    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface TransformationSingleThread {}

    /**
     * A {@link com.google.inject.BindingAnnotation} for injecting the transformation phase
     * executor.
     */
    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface TransformationPoolThread {}
  }
}
