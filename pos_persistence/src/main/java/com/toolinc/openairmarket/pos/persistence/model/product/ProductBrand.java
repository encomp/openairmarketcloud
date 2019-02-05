package com.toolinc.openairmarket.pos.persistence.model.product;

import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.persistence.model.AbstractCatalogModel;

/** Define the different brand of a {@link ProductManufacturer}. */
public final class ProductBrand extends AbstractCatalogModel {

  /**
   * Creates a new {@link Builder} instance.
   *
   * @return - new instance
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /** Builder class that creates instances of {@link ProductBrand}. */
  public static final class Builder implements Domain {

    private String referenceId;
    private String name;

    public Builder setReferenceId(String referenceId) {
      this.referenceId = checkNotEmpty(referenceId);
      return this;
    }

    public Builder setName(String name) {
      this.name = checkNotEmpty(name);
      return this;
    }

    /**
     * Creates a new instance of {@link ProductBrand}.
     *
     * @return - new instance
     */
    public ProductBrand build() {
      ProductBrand company = new ProductBrand();
      company.setReferenceId(referenceId);
      company.setName(name);
      return company;
    }
  }
}
