package com.toolinc.openairmarket.etl.pipeline.step;

import com.toolinc.openairmarket.etl.pipeline.model.Script;
import java.util.List;

/** Specifies the behavior of runner of tasks for a particular step of a pipeline */
public interface StepExecutor {

  /**
   * Specifies the list of the desire scripts that needs to be executed.
   *
   * @param scripts the list of scripts to be executed.
   * @param path the path from in which the scripts are being stored.
   * @throws {@link StepException} if the runner cannot be performed.
   */
  void executeStep(List<Script> scripts, String path) throws StepException;
}
