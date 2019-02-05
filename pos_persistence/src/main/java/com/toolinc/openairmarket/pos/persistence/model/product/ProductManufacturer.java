package com.toolinc.openairmarket.pos.persistence.model.product;

import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.persistence.model.AbstractCatalogModel;

/** Define the different companies of a {@link ProductType}. */
public final class ProductManufacturer extends AbstractCatalogModel {

  /**
   * Creates a new {@link Builder} instance.
   *
   * @return - new instance
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /** Builder class that creates instances of {@link ProductManufacturer}. */
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
     * Creates a new instance of {@link ProductManufacturer}.
     *
     * @return - new instance
     */
    public ProductManufacturer build() {
      ProductManufacturer company = new ProductManufacturer();
      company.setReferenceId(referenceId);
      company.setName(name);
      return company;
    }
  }
}
