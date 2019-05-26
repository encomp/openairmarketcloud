package com.toolinc.openairmarket.ui.fragment.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Annotations injection annotations. */
public final class Annotations {

  private Annotations() {}

  /** Specifies the injection for sale */
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  public @interface Sale {

    /** Specifies sale succeeded injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public @interface Succeed {}

    /** Specifies sale failed injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public @interface Failed {}
  }
}
