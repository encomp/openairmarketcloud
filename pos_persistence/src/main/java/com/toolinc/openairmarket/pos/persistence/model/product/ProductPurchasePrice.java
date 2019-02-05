package com.toolinc.openairmarket.pos.persistence.model.product;

import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.persistence.model.AbstractActiveModel;

import java.math.BigDecimal;

/** Specifies the price of a {@link Product} the price of how it should be bought and sold. */
public final class ProductPurchasePrice extends AbstractActiveModel {

  /** The official net price of a product in a specified currency. */
  private BigDecimal listPrice;

  /** The regular or normal price of a product in the respective price list. */
  private BigDecimal price;

  /** The lowest net price a specified item may be sold for. */
  private BigDecimal limitPrice;

  public BigDecimal getListPrice() {
    return listPrice;
  }

  public void setListPrice(String listPrice) {
    this.listPrice = checkPositive(new BigDecimal(listPrice));
  }

  public BigDecimal listPrice() {
    return limitPrice;
  }

  public void listPrice(BigDecimal listPrice) {
    this.listPrice = checkPositive(listPrice);
  }

  public String getPrice() {
    return toStringMoney(price);
  }

  public void setPrice(String price) {
    this.price = checkPositive(new BigDecimal(price));
  }

  public BigDecimal price() {
    return this.price;
  }

  public void price(BigDecimal price) {
    this.price = checkPositive(price);
  }

  public String getLimitPrice() {
    return toStringMoney(limitPrice);
  }

  public void setLimitPrice(String limitPrice) {
    this.limitPrice = checkPositive(new BigDecimal(limitPrice));
  }

  public BigDecimal limitPrice() {
    return this.limitPrice;
  }

  public void limitPrice(BigDecimal limitPrice) {
    this.limitPrice = checkPositive(limitPrice);
  }

  /**
   * Creates a new {@link Builder} instance.
   *
   * @return - new instance
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /** Builder class that creates instances of {@link ProductPurchasePrice}. */
  public static final class Builder implements Domain {

    private BigDecimal price;

    public Builder setPrice(String price) {
      BigDecimal bigDecimal = new BigDecimal(price.trim());
      return setPrice(bigDecimal);
    }

    public Builder setPrice(BigDecimal price) {
      this.price = checkPositive(price);
      return this;
    }

    /**
     * Creates a new instance of {@link ProductPurchasePrice}.
     *
     * @return - new instance
     */
    public ProductPurchasePrice build() {
      ProductPurchasePrice productPurchasePrice = new ProductPurchasePrice();
      productPurchasePrice.price(price);
      return productPurchasePrice;
    }
  }
}
