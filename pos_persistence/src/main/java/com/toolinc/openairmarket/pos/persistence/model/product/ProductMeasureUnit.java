package com.toolinc.openairmarket.pos.persistence.model.product;

import com.google.common.base.Preconditions;
import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.persistence.model.AbstractCatalogModel;

/** Define the different measure units of a {@link ProductType}. */
public final class ProductMeasureUnit extends AbstractCatalogModel {

  private Boolean countable;

  private Boolean expire;

  public Boolean getCountable() {
    return countable;
  }

  public void setCountable(Boolean countable) {
    this.countable = Preconditions.checkNotNull(countable);
  }

  public Boolean getExpire() {
    return expire;
  }

  public void setExpire(Boolean expire) {
    this.expire = Preconditions.checkNotNull(expire);
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
    private Boolean expire;

    public Builder setReferenceId(String referenceId) {
      this.referenceId = checkNotEmpty(referenceId);
      return this;
    }

    public Builder setName(String name) {
      this.name = checkNotEmpty(name);
      return this;
    }

    public Builder setCountable(Boolean countable) {
      this.countable = Preconditions.checkNotNull(countable);
      return this;
    }

    public Builder setExpire(Boolean expire) {
      this.expire = Preconditions.checkNotNull(expire);
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
      paquete.setExpire(expire);
      return paquete;
    }
  }
}
