package com.toolinc.openairmarket.etl.pipeline.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.toolinc.openairmarket.etl.pipeline.model.Pipelines.Pipeline;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;

/** Helper that provides all the particular elements of a pipelines given a specific identifier. */
@Singleton
public final class PipelineHelper {

  private final Pipelines pipelines;
  private final Map<String, Pipeline> pipeline;
  private final Map<String, Extract> extracts;
  private final Map<String, Input> inputs;
  private final Map<String, Conversion> conversions;

  @Inject
  public PipelineHelper(Pipelines pipelines) {
    this.pipelines = Preconditions.checkNotNull(pipelines);
    pipeline = createMap(pipelines.getPipeline());
    extracts = createMap(pipelines.getExtracts().getExtract());
    inputs = createMap(pipelines.getInputs().getInput());
    conversions = createMap(pipelines.getConversions().getConversion());
  }

  /**
   * Search for a particular pipelines by its identifier.
   *
   * @param id the identifier.
   * @return the {@code Workflow}
   */
  public Pipeline getPipeline(String id) {
    return pipeline.get(id);
  }

  /**
   * Specifies the path in which the extract scripts are stored.
   *
   * @return the path of the scripts.
   */
  public String getExtractPath() {
    return pipelines.getExtracts().getPath();
  }

  /**
   * Search for a particular all the extract scripts of a pipelines.
   *
   * @param workflowId the identifier
   * @return a list of {@code Script}.
   */
  public List<Script> getExtracts(String workflowId) {
    ImmutableList.Builder<Script> builder = ImmutableList.builder();
    Extracts extract = getPipeline(workflowId).getExtracts();
    List<Extract> extracts = createList(extract.getExtract(), this.extracts);
    for (Extract tmpExtract : extracts) {
      builder.addAll(tmpExtract.getScripts().getScript());
    }
    return builder.build();
  }

  /**
   * Specifies the path in which the input scripts are stored.
   *
   * @return the path of the scripts.
   */
  public String getInputPath() {
    return pipelines.getInputs().getPath();
  }

  /**
   * Search for a particular all the input scripts of a pipelines.
   *
   * @param workflowId the identifier
   * @return a list of {@code Script}.
   */
  public List<Script> getInputs(String workflowId) {
    ImmutableList.Builder<Script> builder = ImmutableList.builder();
    Inputs input = getPipeline(workflowId).getTransformations().getInputs();
    List<Input> inputs = createList(input.getInput(), this.inputs);
    for (Input tmpInput : inputs) {
      builder.addAll(tmpInput.getScripts().getScript());
    }
    return builder.build();
  }

  /**
   * Search for a particular all the input scripts of a pipelines.
   *
   * @param inputIds the identifier
   * @return a list of {@code Script}.
   */
  public List<Script> getInputs(List<Input> inputIds) {
    ImmutableList.Builder<Script> builder = ImmutableList.builder();
    List<Input> inputs = createList(inputIds, this.inputs);
    for (Input tmpInput : inputs) {
      builder.addAll(tmpInput.getScripts().getScript());
    }
    return builder.build();
  }

  /**
   * Search for a particular all the conversion scripts of a pipelines.
   *
   * @param workflowId the identifier
   * @return a list of {@code Script}.
   */
  public List<Script> getConversions(String workflowId) {
    ImmutableList.Builder<Script> builder = ImmutableList.builder();
    Conversions conversion = getPipeline(workflowId).getTransformations().getConversions();
    List<Conversion> conversions = createList(conversion.getConversion(), this.conversions);
    for (Conversion tmpConversion : conversions) {
      builder.addAll(tmpConversion.getScripts().getScript());
    }
    return builder.build();
  }

  /**
   * Specifies the path in which the conversion scripts are stored.
   *
   * @return the path of the scripts.
   */
  public String getConversionPath() {
    return pipelines.getConversions().getPath();
  }

  private <T extends Identifier> Map<String, T> createMap(List<T> list) {
    ImmutableMap.Builder<String, T> iBuilder = ImmutableMap.builder();
    for (T element : list) {
      iBuilder.put(element.getId(), element);
    }
    return iBuilder.build();
  }

  private <T extends Identifier> List<T> createList(List<T> identifiers, Map<String, T> map) {
    ImmutableList.Builder<T> iBuilder = ImmutableList.builder();
    for (Identifier identifier : identifiers) {
      T extractTemp = map.get(identifier.getId());
      Preconditions.checkNotNull(extractTemp);
      iBuilder.add(extractTemp);
    }
    return iBuilder.build();
  }
}
