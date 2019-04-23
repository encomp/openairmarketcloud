package com.toolinc.openairmarket.persistence.cloud.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Repository injection annotations. */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Repository {

  /** Specifies the injection for product */
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  public @interface Product {

    /** Specifies product brands injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    @interface Brands {}

    /** Specifies product categories injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    @interface Categories {}

    /** Specifies product manufacturers injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    @interface Manufacturers {}

    /** Specifies products injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    @interface Products {}

    /** Specifies product manufacturers injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    @interface Units {}
  }
}
