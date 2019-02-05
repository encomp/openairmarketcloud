package com.toolinc.openairmarket.etl.pipeline.runner;

import com.google.common.base.Preconditions;
import com.google.common.flogger.FluentLogger;
import com.toolinc.openairmarket.etl.pipeline.step.ExtractStep;
import com.toolinc.openairmarket.etl.pipeline.step.StepException;
import javax.inject.Inject;

/** Executes only the extraction step of a particular pipeline. * */
public final class ExtractPipelineRunner implements PipelineRunner {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final ExtractStep extractStep;

  @Inject
  public ExtractPipelineRunner(ExtractStep extractStep) {
    this.extractStep = Preconditions.checkNotNull(extractStep);
  }

  @Override
  public void execute(String id) throws PipelineRunnerException {
    try {
      logger.atInfo().log(String.format("Starting the extraction runner [%s].", id));
      extractStep.execute(id);
      logger.atInfo().log(String.format("Finished the extraction runner [%s].", id));
    } catch (StepException exc) {
      String msg =
          String.format(
              "An unexpected error occurred while executing the extraction runner [%s].", id);
      throw new PipelineRunnerException(msg, exc);
    }
  }
}
