package com.toolinc.openairmarket.etl.pipeline.inject;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.toolinc.openairmarket.etl.ConversionService;
import com.toolinc.openairmarket.etl.ExtractService;
import com.toolinc.openairmarket.etl.database.H2DataBaseHelper;
import com.toolinc.openairmarket.etl.pipeline.inject.BindingAnnotations.Paths;
import com.toolinc.openairmarket.etl.pipeline.inject.BindingAnnotations.StepExecutors;
import com.toolinc.openairmarket.etl.pipeline.model.ObjectFactory;
import com.toolinc.openairmarket.etl.pipeline.model.PipelineHelper;
import com.toolinc.openairmarket.etl.pipeline.model.Pipelines;
import com.toolinc.openairmarket.etl.pipeline.runner.ExtractPipelineRunner;
import com.toolinc.openairmarket.etl.pipeline.runner.PlainPipelineRunner;
import com.toolinc.openairmarket.etl.pipeline.step.ConversionStep;
import com.toolinc.openairmarket.etl.pipeline.step.ExtractStep;
import com.toolinc.openairmarket.etl.pipeline.step.InputStep;
import com.toolinc.openairmarket.etl.pipeline.step.StepExecutor;
import com.toolinc.openairmarket.etl.pipeline.step.impl.ConversionStepExecutor;
import com.toolinc.openairmarket.etl.pipeline.step.impl.ConversionStepImpl;
import com.toolinc.openairmarket.etl.pipeline.step.impl.ExtractStepExecutor;
import com.toolinc.openairmarket.etl.pipeline.step.impl.ExtractStepImpl;
import com.toolinc.openairmarket.etl.pipeline.step.impl.InputStepImpl;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/** Bindings for the pipeline module. */
public class PipelineModule extends AbstractModule {

  private final String pipelineConfig;
  private final String scriptsPath;
  private final String inputPath;
  private final String outputPath;

  public PipelineModule(
      String pipelineConfig, String scriptsPath, String inputPath, String outputPath) {
    this.pipelineConfig = Preconditions.checkNotNull(pipelineConfig);
    this.scriptsPath = Preconditions.checkNotNull(scriptsPath);
    this.inputPath = Preconditions.checkNotNull(inputPath);
    this.outputPath = Preconditions.checkNotNull(outputPath);
  }

  @Override
  protected void configure() {
    requireBinding(ExtractService.class);
    requireBinding(ConversionService.class);
    requireBinding(H2DataBaseHelper.class);
    // binding paths
    bind(String.class).annotatedWith(Paths.PipelineConfig.class).toInstance(pipelineConfig);
    bind(String.class).annotatedWith(Paths.Scripts.class).toInstance(scriptsPath);
    bind(String.class).annotatedWith(Paths.Input.class).toInstance(inputPath);
    bind(String.class).annotatedWith(Paths.Output.class).toInstance(outputPath);
    // binding pipeline components
    bind(PipelineHelper.class);
    bind(StepExecutor.class)
        .annotatedWith(StepExecutors.Extraction.class)
        .to(ExtractStepExecutor.class);
    bind(ExtractStep.class).to(ExtractStepImpl.class);
    bind(InputStep.class).to(InputStepImpl.class);
    bind(ConversionStep.class).to(ConversionStepImpl.class);
    // binding pipeline runners
    bind(ExtractPipelineRunner.class);
    bind(PlainPipelineRunner.class);
  }

  @Provides
  @Singleton
  @SuppressWarnings({"unchecked", "cast"})
  public Pipelines providesPipelines(@Paths.PipelineConfig String pipelinePath) {
    try (InputStream inputStream = Files.newInputStream(java.nio.file.Paths.get(pipelinePath))) {
      JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
      final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      return ((JAXBElement<Pipelines>) unmarshaller.unmarshal(inputStream)).getValue();
    } catch (IOException | JAXBException exc) {
      throw new IllegalStateException(
          String.format("Unable to read the pipeline configuration file [%s].", pipelinePath), exc);
    }
  }

  @Provides
  public ExecutorService providesExecutorService() {
    return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  }

  @Provides
  @StepExecutors.TransformationPoolThread
  public StepExecutor providesConversionPoolThread(
      Provider<ConversionService> conversionServiceProvider,
      ExecutorService executorService,
      @Paths.Scripts String basePath) {
    return new ConversionStepExecutor(conversionServiceProvider, executorService, basePath);
  }

  @Provides
  @StepExecutors.TransformationSingleThread
  public StepExecutor providesConversionSingleThread(
      Provider<ConversionService> transformationService, @Paths.Scripts String basePath) {
    return new ConversionStepExecutor(
        transformationService, Executors.newSingleThreadExecutor(), basePath);
  }
}
