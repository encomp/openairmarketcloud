package com.toolinc.openairmarket.etl.pipeline.inject;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import javax.inject.Singleton;

/** Specifies the command line arguments for the pipeline module. */
@Singleton
public class PipelineOptions extends OptionsBase {

  @Option(
      name = "pipelineConfig",
      help = "Specifies the path of the pipeline configuration file.",
      category = "startup",
      defaultValue =
          "/Users/edgarrico/Documents/openairmarket/java/etl/target/pipeline/config/pipeline.xml")
  public String pipelineConfig;

  @Option(
      name = "pipelineRunner",
      help = "Specifies the desire runner that will be used to execute a pipeline.",
      category = "startup",
      defaultValue = "extract")
  public String pipelineRunner;

  @Option(
      name = "pipelineId",
      help = "Specifies the desire pipeline id that will be executed.",
      category = "startup",
      defaultValue = "productos")
  public String pipelineId;

  @Option(
      name = "scriptsPath",
      help = "Specifies the path in which the scripts are being stored.",
      category = "startup",
      defaultValue = "/Users/edgarrico/Documents/openairmarket/java/etl/target/pipeline/")
  public String scriptsPath;

  @Option(
      name = "inputPath",
      help = "Specifies the path in which the information extracted from MSSQL will be stored.",
      category = "startup",
      defaultValue = "pipeline/data/input/")
  public String inputPath;

  @Option(
      name = "outputPath",
      help = "Specifies the path in which the information extracted from H2 will be stored.",
      category = "startup",
      defaultValue =
          "/Users/edgarrico/Documents/openairmarket/java/etl/target/pipeline/data/output/")
  public String outputPath;
}
