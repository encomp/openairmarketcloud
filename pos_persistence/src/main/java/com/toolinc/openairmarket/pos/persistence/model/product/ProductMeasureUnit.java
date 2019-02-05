package com.toolinc.openairmarket.pos.persistence.model.product;

import com.google.common.base.Preconditions;
import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.persistence.model.AbstractCatalogModel;

/** Define the different measure units of a {@link ProductType}. */
public final class ProductMeasureUnit extends AbstractCatalogModel {

  private String id;

  private Boolean countable;

  public String id() {
    return id;
  }

  public void setId(String id) {
    this.id = checkNotEmpty(id);
  }

  public Boolean getCountable() {
    return countable;
  }

  public void setCountable(Boolean countable) {
    this.countable = Preconditions.checkNotNull(countable);
  }

  /**
   * Creates a new {@link Builder} instance.
   *
   * @return - new instance
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /** Builder class that creates instances of {@link ProductMeasureUnit}. */
  public static final class Builder implements Domain {

    private String referenceId;
    private String name;
    private Boolean countable;

    public Builder setReferenceId(String referenceId) {
      this.referenceId = checkNotEmpty(referenceId).toUpperCase();
      return this;
    }

    public Builder setName(String name) {
      this.name = checkNotEmpty(name).toUpperCase();
      return this;
    }

    public Builder setCountable(Boolean countable) {
      this.countable = Preconditions.checkNotNull(countable);
      return this;
    }

    /**
     * Creates a new instance of {@link ProductMeasureUnit}.
     *
     * @return - new instance
     */
    public ProductMeasureUnit build() {
      ProductMeasureUnit paquete = new ProductMeasureUnit();
      paquete.setReferenceId(referenceId);
      paquete.setName(name);
      paquete.setCountable(countable);
      return paquete;
    }
  }
}
