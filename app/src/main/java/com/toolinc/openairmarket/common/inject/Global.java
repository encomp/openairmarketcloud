package com.toolinc.openairmarket.common.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** App global annotations. */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Global {

  /** Specifies an disk I/O operation. */
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  @interface DiskIO {}

  /** Specifies a network I/O operation. */
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  @interface NetworkIO {}

  /** Specifies a main thread operation. */
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  @interface MainThread {}
}
