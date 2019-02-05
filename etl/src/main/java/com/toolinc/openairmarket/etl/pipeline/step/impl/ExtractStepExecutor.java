package com.toolinc.openairmarket.etl.pipeline.step.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.toolinc.openairmarket.etl.pipeline.inject.BindingAnnotations.Paths;

import com.google.common.collect.ImmutableList;
import com.google.common.flogger.FluentLogger;
import com.toolinc.openairmarket.etl.ExtractService;
import com.toolinc.openairmarket.etl.pipeline.model.Script;
import com.toolinc.openairmarket.etl.pipeline.step.StepException;
import com.toolinc.openairmarket.etl.pipeline.step.StepExecutor;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import javax.inject.Provider;

/** Executes a set of scripts using an {@code ExtractService}. */
public final class ExtractStepExecutor implements StepExecutor {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final Provider<ExtractService> extractService;
  private final ExecutorService executorService;
  private final String basePath;
  private final String outputPath;

  @Inject
  public ExtractStepExecutor(
      Provider<ExtractService> extractService,
      ExecutorService executorService,
      @Paths.Scripts String basePath,
      @Paths.Input String outputPath) {
    checkState(!isNullOrEmpty(basePath));
    checkState(!isNullOrEmpty(outputPath));
    this.extractService = checkNotNull(extractService, "ExtractService provider is missing.");
    this.executorService = checkNotNull(executorService, "ExecutorService is missing.");
    this.basePath = basePath;
    this.outputPath = outputPath;
  }

  @Override
  public void executeStep(List<Script> scripts, String inputPath) throws StepException {
    logger.atFiner().log(String.format("Starts the runner of [%s].", scripts));
    ImmutableList.Builder<Callable<Boolean>> tasks = ImmutableList.builder();
    final CountdownStepExecutor countdownStepExecutor =
        CountdownStepExecutor.build(executorService, scripts.size());
    for (Script script : scripts) {
      CallableScript callableScript =
          new CallableScript(
              countdownStepExecutor, extractService.get(), script, basePath, inputPath, outputPath);
      tasks.add(callableScript);
    }
    countdownStepExecutor.execute(tasks.build());
    logger.atFiner().log(String.format("Finished the runner of [%s].", scripts));
  }

  /** Defines a extraction task. */
  private static final class CallableScript implements Callable<Boolean> {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private final CountdownStepExecutor countdownStepExecutor;
    private final ExtractService extractService;
    private final Script script;
    private final String basePath;
    private final String outputPath;
    private final String inputPath;

    public CallableScript(
        CountdownStepExecutor countdownStepExecutor,
        ExtractService extractService,
        Script script,
        String basePath,
        String inputPath,
        String outputPath) {
      checkState(!isNullOrEmpty(basePath));
      checkState(!isNullOrEmpty(inputPath));
      checkState(!isNullOrEmpty(outputPath));
      this.countdownStepExecutor =
          checkNotNull(countdownStepExecutor, "CountdownStepExecutor is missing.");
      this.extractService = checkNotNull(extractService, "ExtractService is missing.");
      this.script = checkNotNull(script, "Script is missing.");
      this.basePath = basePath;
      this.inputPath = inputPath;
      this.outputPath = outputPath;
    }

    @Override
    public Boolean call() throws Exception {
      String scriptPath = String.format("%s%s%s", basePath, inputPath, script.getValue());
      try {
        logger.atInfo().log(String.format("About to execute the extract script [%s].", scriptPath));
        boolean flag = extractService.extract(scriptPath, outputPath, script.getOutputName());
        countdownStepExecutor.countDown();
        logger.atInfo().log(
            String.format("Finished the executing of the extract script [%s].", scriptPath));
        return flag;
      } catch (Exception exc) {
        countdownStepExecutor.cancelExecution();
        throw exc;
      }
    }
  }
}
