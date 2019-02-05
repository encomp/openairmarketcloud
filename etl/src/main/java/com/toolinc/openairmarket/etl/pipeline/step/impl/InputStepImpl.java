package com.toolinc.openairmarket.etl.pipeline.step.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.flogger.FluentLogger;
import com.toolinc.openairmarket.etl.pipeline.inject.BindingAnnotations.StepExecutors;
import com.toolinc.openairmarket.etl.pipeline.model.PipelineHelper;
import com.toolinc.openairmarket.etl.pipeline.model.Script;
import com.toolinc.openairmarket.etl.pipeline.step.InputStep;
import com.toolinc.openairmarket.etl.pipeline.step.StepException;
import com.toolinc.openairmarket.etl.pipeline.step.StepExecutor;
import java.util.List;
import javax.inject.Inject;

/** Defines the behavior of the input step for a pipeline. */
public class InputStepImpl implements InputStep {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final PipelineHelper pipelineHelper;
  private final StepExecutor stepExecutor;

  @Inject
  public InputStepImpl(
      PipelineHelper pipelineHelper,
      @StepExecutors.TransformationPoolThread StepExecutor stepExecutor) {
    this.pipelineHelper = checkNotNull(pipelineHelper, "PipelineHelper is missing.");
    this.stepExecutor = checkNotNull(stepExecutor, "StepExecutor is missing.");
  }

  @Override
  public void execute(String id) throws StepException {
    logger.atInfo().log(
        String.format("Start the runner of Input Phase for the workflow [%s].", id));
    List<Script> inputs = pipelineHelper.getInputs(id);
    stepExecutor.executeStep(inputs, pipelineHelper.getInputPath());
    logger.atInfo().log(
        String.format("Finished the runner of Input Phase for the workflow [%s].", id));
  }
}
