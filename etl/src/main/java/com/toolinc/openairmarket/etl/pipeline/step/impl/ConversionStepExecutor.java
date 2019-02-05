package com.toolinc.openairmarket.etl.pipeline.step.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.isNullOrEmpty;

import com.google.common.collect.ImmutableList;
import com.google.common.flogger.FluentLogger;
import com.toolinc.openairmarket.etl.ConversionService;
import com.toolinc.openairmarket.etl.pipeline.inject.BindingAnnotations.Paths;
import com.toolinc.openairmarket.etl.pipeline.model.Script;
import com.toolinc.openairmarket.etl.pipeline.step.StepException;
import com.toolinc.openairmarket.etl.pipeline.step.StepExecutor;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import javax.inject.Provider;

/** Executes a set of scripts using a {@link ConversionService}. */
public final class ConversionStepExecutor implements StepExecutor {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final ExecutorService executorService;
  private final Provider<ConversionService> conversionService;
  private final String basePath;

  @Inject
  public ConversionStepExecutor(
      Provider<ConversionService> conversionService,
      ExecutorService executorService,
      @Paths.Scripts String basePath) {
    checkState(!isNullOrEmpty(basePath));
    this.conversionService = checkNotNull(conversionService, "ConversionService is missing.");
    this.executorService = checkNotNull(executorService, "ExecutorService is missing.");
    this.basePath = basePath;
  }

  @Override
  public void executeStep(List<Script> scripts, String path) throws StepException {
    logger.atFiner().log(String.format("Starts the runner of [%s].", scripts));
    ImmutableList.Builder<Callable<Boolean>> tasks = ImmutableList.builder();
    CountdownStepExecutor phaseExecutor =
        CountdownStepExecutor.build(executorService, scripts.size());
    for (Script script : scripts) {
      CallableScript callableScript =
          new CallableScript(phaseExecutor, conversionService.get(), script, basePath, path);
      tasks.add(callableScript);
    }
    phaseExecutor.execute(tasks.build());
    logger.atFiner().log(String.format("Finished the runner of [%s].", scripts));
  }

  /** Defines a transformation task. */
  private static final class CallableScript implements Callable<Boolean> {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private final CountdownStepExecutor countdownStepExecutor;
    private final ConversionService conversionService;
    private final Script script;
    private final String basePath;
    private final String scriptPath;

    public CallableScript(
        CountdownStepExecutor countdownStepExecutor,
        ConversionService conversionService,
        Script script,
        String basePath,
        String scriptPath) {
      checkState(!isNullOrEmpty(basePath));
      checkState(!isNullOrEmpty(scriptPath));
      this.countdownStepExecutor =
          checkNotNull(countdownStepExecutor, "CountdownStepExecutor is missing.");
      this.conversionService = checkNotNull(conversionService, "ConversionService is missing.");
      this.script = checkNotNull(script, "Script is missing.");
      this.basePath = basePath;
      this.scriptPath = scriptPath;
    }

    @Override
    public Boolean call() throws Exception {
      String fullPath = String.format("%s%s%s", basePath, scriptPath, script.getValue());
      try {
        logger.atInfo().log(String.format("About to execute the script [%s].", fullPath));
        boolean flag = conversionService.transform(fullPath);
        countdownStepExecutor.countDown();
        logger.atInfo().log(String.format("Finished the executing of the script [%s].", fullPath));
        return flag;
      } catch (Exception exc) {
        logger.atInfo().log(
            String.format("An unexpected error occurred executing the script [%s].", fullPath));
        countdownStepExecutor.cancelExecution();
        throw exc;
      }
    }
  }
}
