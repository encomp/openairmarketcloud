package com.toolinc.openairmarket.etl.pipeline.runner;

/** Marker interface that specifies the behavior of a pipeline runner. */
public interface PipelineRunner {

  /**
   * Kickoff the runner of a particular pipeline.
   *
   * @param id specifies the desired pipeline id that will be executed.
   * @throws PipelineRunnerException in case of a failure or interruption of a pipeline.
   */
  void execute(String id) throws PipelineRunnerException;
}
