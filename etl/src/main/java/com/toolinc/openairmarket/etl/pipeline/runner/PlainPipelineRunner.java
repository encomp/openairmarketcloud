package com.toolinc.openairmarket.etl.pipeline.runner;

import com.google.common.base.Preconditions;
import com.google.common.flogger.FluentLogger;
import com.toolinc.openairmarket.etl.pipeline.step.ConversionStep;
import com.toolinc.openairmarket.etl.pipeline.step.InputStep;
import com.toolinc.openairmarket.etl.pipeline.step.StepException;
import javax.inject.Inject;

/** Executes all the steps (extract, input, conversion) of a one pipeline. * */
public final class PlainPipelineRunner implements PipelineRunner {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final ExtractPipelineRunner extractExecution;
  private final InputStep inputStep;
  private final ConversionStep conversionStep;

  @Inject
  public PlainPipelineRunner(
      ExtractPipelineRunner extractExecution, InputStep inputStep, ConversionStep conversionStep) {
    this.extractExecution = Preconditions.checkNotNull(extractExecution);
    this.inputStep = Preconditions.checkNotNull(inputStep);
    this.conversionStep = Preconditions.checkNotNull(conversionStep);
  }

  @Override
  public void execute(String id) throws PipelineRunnerException {
    try {
      logger.atInfo().log(String.format("Starting the pipeline runner [%s].", id));
      extractExecution.execute(id);
      inputStep.execute(id);
      conversionStep.execute(id);
      logger.atInfo().log(String.format("Finished the pipeline runner [%s].", id));
    } catch (StepException exc) {
      String msg =
          String.format(
              "An unexpected error occurred while executing the pipeline runner [%s].", id);
      throw new PipelineRunnerException(msg, exc);
    }
  }
}
