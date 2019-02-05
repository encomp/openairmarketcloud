package com.toolinc.openairmarket.etl.pipeline.step;

/** Marker interface that specifies the behavior of a flow for the migration pipeline. */
public interface Step {

  /**
   * Kickoff the runner of a particular runner.
   *
   * @param id specifies the desired workflow id that will be executed.
   * @throws StepException in case of a failure or interruption of a {@code PipelineRunner}.
   */
  void execute(String id) throws StepException;
}
