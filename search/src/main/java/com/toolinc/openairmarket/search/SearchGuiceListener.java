package com.toolinc.openairmarket.search;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/** Specifies the servlet context listener to make use of guice. */
public class SearchGuiceListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new SearchEndpointModule());
  }
}
