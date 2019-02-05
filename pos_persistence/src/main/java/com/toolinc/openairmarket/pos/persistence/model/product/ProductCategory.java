package com.toolinc.openairmarket.pos.persistence.model.product;

import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.persistence.model.AbstractCatalogModel;

/** Define the different categories in which a {@link Product} can be assigned. */
public final class ProductCategory extends AbstractCatalogModel {

  /**
   * Creates a new {@link ProductManufacturer.Builder} instance.
   *
   * @return - new instance
   */
  public static ProductManufacturer.Builder newBuilder() {
    return new ProductManufacturer.Builder();
  }

  /** Builder class that creates instances of {@link ProductCategory}. */
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
     * Creates a new instance of {@link ProductCategory}.
     *
     * @return - new instance
     */
    public ProductCategory build() {
      ProductCategory company = new ProductCategory();
      company.setReferenceId(referenceId);
      company.setName(name);
      return company;
    }
  }
}
