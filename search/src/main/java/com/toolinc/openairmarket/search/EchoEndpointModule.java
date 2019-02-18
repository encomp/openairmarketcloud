package com.toolinc.openairmarket.search;

import com.google.api.control.ServiceManagementConfigFilter;
import com.google.api.control.extensions.appengine.GoogleAppEngineControlFilter;
import com.google.api.server.spi.guice.EndpointsModule;
import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

public class EchoEndpointModule extends EndpointsModule {
  @Override
  public void configureServlets() {
    super.configureServlets();

    bind(ServiceManagementConfigFilter.class).in(Singleton.class);
    filter("/_ah/api/*").through(ServiceManagementConfigFilter.class);

    Map<String, String> apiController = new HashMap<String, String>();
    apiController.put("endpoints.projectId", "openairmarket-150121");
    apiController.put("endpoints.serviceName", "openairmarket-150121.appspot.com");

    bind(GoogleAppEngineControlFilter.class).in(Singleton.class);
    filter("/_ah/api/*").through(GoogleAppEngineControlFilter.class, apiController);

    bind(Echo.class).toInstance(new Echo());
    configureEndpoints("/_ah/api/*", ImmutableList.of(Echo.class));
  }
}
