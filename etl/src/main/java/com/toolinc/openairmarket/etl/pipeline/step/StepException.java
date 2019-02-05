package com.toolinc.openairmarket.etl.pipeline.step;

/** This type of exception is produced by failure or interrupted runner of a {@link Step}. */
public class StepException extends Exception {

  public StepException(String message) {
    super(message);
  }

  public StepException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
