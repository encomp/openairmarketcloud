package com.toolinc.openairmarket.etl.pipeline.step.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.flogger.FluentLogger;
import com.toolinc.openairmarket.etl.pipeline.inject.BindingAnnotations.StepExecutors;
import com.toolinc.openairmarket.etl.pipeline.model.PipelineHelper;
import com.toolinc.openairmarket.etl.pipeline.model.Script;
import com.toolinc.openairmarket.etl.pipeline.step.ExtractStep;
import com.toolinc.openairmarket.etl.pipeline.step.StepException;
import com.toolinc.openairmarket.etl.pipeline.step.StepExecutor;
import java.util.List;
import javax.inject.Inject;

/** Defines the behavior of the extract step for a pipeline. */
public class ExtractStepImpl implements ExtractStep {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final PipelineHelper pipelineHelper;
  private final StepExecutor stepExecutor;

  @Inject
  public ExtractStepImpl(
      PipelineHelper pipelineHelper, @StepExecutors.Extraction StepExecutor stepExecutor) {
    this.pipelineHelper = checkNotNull(pipelineHelper, "PipelineHelper is missing.");
    this.stepExecutor = checkNotNull(stepExecutor, "StepExecutor is missing.");
  }

  @Override
  public void execute(String id) throws StepException {
    logger.atInfo().log(
        String.format("Start the runner of Extract Phase for the workflow [%s].", id));
    List<Script> scripts = pipelineHelper.getExtracts(id);
    stepExecutor.executeStep(scripts, pipelineHelper.getExtractPath());
    logger.atInfo().log(
        String.format("Finished the runner of Extract Phase for the workflow [%s].", id));
  }
}
