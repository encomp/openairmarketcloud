package com.toolinc.openairmarket.search;

import com.google.api.control.ServiceManagementConfigFilter;
import com.google.api.control.extensions.appengine.GoogleAppEngineControlFilter;
import com.google.api.server.spi.guice.EndpointsModule;
import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

/** Defines the injection points required for the {@link Search} api. */
public class SearchEndpointModule extends EndpointsModule {

  private static final String PROJECT_ID_KEY = "endpoints.projectId";
  private static final String ENDPOINT_KEY = "endpoints.serviceName";
  private static final String BASE_URL = "/_ah/api/*";
  private static final String PROJECT_ID = "openairmarket-150121";
  private static final String ENDPOINT = "openairmarket-150121.appspot.com";

  @Override
  public void configureServlets() {
    super.configureServlets();

    bind(ServiceManagementConfigFilter.class).in(Singleton.class);
    filter(BASE_URL).through(ServiceManagementConfigFilter.class);

    Map<String, String> apiController = new HashMap<String, String>();
    apiController.put(PROJECT_ID_KEY, PROJECT_ID);
    apiController.put(ENDPOINT_KEY, ENDPOINT);

    bind(GoogleAppEngineControlFilter.class).in(Singleton.class);
    filter(BASE_URL).through(GoogleAppEngineControlFilter.class, apiController);

    bind(Search.class).toInstance(new Search());
    configureEndpoints(BASE_URL, ImmutableList.of(Search.class));
  }
}
