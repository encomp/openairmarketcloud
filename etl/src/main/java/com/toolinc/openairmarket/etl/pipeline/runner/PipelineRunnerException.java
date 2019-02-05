package com.toolinc.openairmarket.etl.pipeline.runner;

/**
 * This type of exception is produced by failure or interrupted runner of a {@link PipelineRunner}.
 */
public class PipelineRunnerException extends Exception {

  public PipelineRunnerException(String message) {
    super(message);
  }

  public PipelineRunnerException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
