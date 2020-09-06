package com.toolinc.openairmarket.pos.persistence.model.ui;

import com.google.common.base.Strings;
import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.persistence.model.AbstractModel;

/**
 * Stores the information of the quick access products for the pos screen.
 */
public class QuickAccessProduct extends AbstractModel {

  /**
   * Stores the user id
   **/
  private String id;
  private String label;
  private Integer position;
  private String product;

  /**
   * Creates a new {@link Builder} instance.
   *
   * @return - new instance
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  public String id() {
    return id;
  }

  public void setId(String id) {
    this.id = checkNotEmpty(id);
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = checkNotEmpty(label).toUpperCase();
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = checkPositive(position);
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = checkNotEmpty(product);
  }

  /**
   * Builder class that creates instances of {@link QuickAccessProduct}.
   */
  public static final class Builder implements Domain {

    private String id;
    private String label;
    private Integer position;
    private String product;

    private Builder() {
    }

    public Builder setId(String id) {
      this.id = checkNotEmpty(id);
      return this;
    }

    public Builder setLabel(String label) {
      this.label = checkNotEmpty(label).toUpperCase();
      return this;
    }

    public Builder setPosition(Integer position) {
      this.position = checkPositive(position);
      return this;
    }

    public Builder setProduct(String product) {
      this.product = checkNotEmpty(product);
      return this;
    }

    /**
     * Creates a new instance of {@link QuickAccessProduct}.
     *
     * @return - new instance
     */
    public QuickAccessProduct build() {
      QuickAccessProduct quickAccessProduct = new QuickAccessProduct();
      if (!Strings.isNullOrEmpty(id)) {
        quickAccessProduct.setId(id);
      }
      quickAccessProduct.setLabel(label);
      quickAccessProduct.setPosition(position);
      quickAccessProduct.setProduct(product);
      return quickAccessProduct;
    }
  }
}
