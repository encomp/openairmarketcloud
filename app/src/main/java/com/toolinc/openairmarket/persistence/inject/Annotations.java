package com.toolinc.openairmarket.persistence.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Annotations injection annotations. */
public final class Annotations {

  private Annotations() {}

  /** Specifies the injection for product */
  public static final class Product {

    private Product() {}

    /** Specifies product brands injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public @interface Brands {}

    /** Specifies product categories injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public @interface Categories {}

    /** Specifies product manufacturers injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public @interface Manufacturers {}

    /** Specifies products injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public @interface Products {}

    /** Specifies product manufacturers injection. */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public @interface Units {}
  }
}
