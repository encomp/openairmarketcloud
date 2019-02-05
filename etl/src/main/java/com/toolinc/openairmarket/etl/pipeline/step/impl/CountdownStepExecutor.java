package com.toolinc.openairmarket.etl.pipeline.step.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.flogger.FluentLogger;
import com.toolinc.openairmarket.etl.pipeline.step.StepException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.inject.Inject;

/** Synchronize the runner of a set of tasks ({@code Callable}) using a {@code CountDownLatch}. */
public final class CountdownStepExecutor {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final ExecutorService executorService;
  private final CountDownLatch countDownLatch;

  @Inject
  private CountdownStepExecutor(ExecutorService executorService, CountDownLatch countDownLatch) {
    this.executorService = checkNotNull(executorService, "ExecutorService is missing.");
    this.countDownLatch = checkNotNull(countDownLatch, "CountDownLatch is missing.");
  }

  /**
   * Starts the runner of a given list of tasks.
   *
   * @param tasks the {@code List} of task to execute.
   * @throws StepException in case of failure during the runner of the phase.
   */
  public void execute(List<Callable<Boolean>> tasks) throws StepException {
    List<Future<Boolean>> results = invokeAll(tasks);
    wait(countDownLatch);
    extractionResult(results);
  }

  /** Cancel the runner of the running tasks. */
  public void cancelExecution() {
    logger.atWarning().log("Start shutting down all the running tasks.");
    executorService.shutdown();
    while (countDownLatch.getCount() >= 1) {
      countDownLatch.countDown();
    }
    logger.atWarning().log("Shutting down has been completed.");
  }

  /** Notifies that a particular task has been completed. */
  public void countDown() {
    countDownLatch.countDown();
  }

  private List<Future<Boolean>> invokeAll(List<Callable<Boolean>> callables) throws StepException {
    try {
      return executorService.invokeAll(callables);
    } catch (InterruptedException e) {
      throw new StepException("Unable to execute the phase.", e);
    }
  }

  private void extractionResult(List<Future<Boolean>> results) throws StepException {
    try {
      for (Future<Boolean> result : results) {
        checkState(result.isDone());
        if (!result.get()) {
          throw new StepException("At least one thread failed.");
        }
      }
    } catch (InterruptedException | ExecutionException e) {
      throw new StepException("An interruption occurred.", e);
    }
  }

  private void wait(CountDownLatch countDownLatch) throws StepException {
    try {
      countDownLatch.await();
    } catch (InterruptedException e) {
      throw new StepException(
          "An interruption occurred while waiting for the phase to complete.", e);
    }
  }

  /**
   * Creates a new instance of {@code CountdownPhaseExecutor}.
   *
   * @param executorService Specifies the {@code ExecutorService} that will be used to execute the
   *     tasks.
   * @param numberOfTask Specifies the number of task to execute.
   * @return a new instance.
   */
  public static CountdownStepExecutor build(ExecutorService executorService, int numberOfTask) {
    CountDownLatch countDownLatch = new CountDownLatch(numberOfTask);
    return new CountdownStepExecutor(executorService, countDownLatch);
  }
}
