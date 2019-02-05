package com.toolinc.openairmarket.pos.persistence.model.product;

import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.persistence.model.AbstractActiveModel;

import java.math.BigDecimal;

/** Specifies the price of a {@link Product} the price of how it should be bought and sold. */
public final class ProductSalePrice extends AbstractActiveModel {

  /** The official net price of a product in a specified currency. */
  private BigDecimal listPrice;

  /** The regular or normal price of a product in the respective price list. */
  private BigDecimal price;

  /** The lowest net price a specified item may be sold for. */
  private BigDecimal limitPrice;

  private BigDecimal profit;

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

  public String getProfit() {
    return toStringMoney(profit);
  }

  public void setProfit(String profit) {
    this.profit = checkPositive(new BigDecimal(profit));
  }

  public BigDecimal profit() {
    return this.profit;
  }

  public void profit(BigDecimal profit) {
    this.profit = checkPositive(profit);
  }

  /**
   * Creates a new {@link Builder} instance.
   *
   * @return - new instance
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /** Builder class that creates instances of {@link ProductSalePrice}. */
  public static final class Builder implements Domain {

    private BigDecimal price;
    private BigDecimal profit;

    public Builder setPrice(BigDecimal price) {
      this.price = checkPositive(price);
      return this;
    }

    public Builder setProfit(BigDecimal profit) {
      this.profit = checkPositive(profit);
      return this;
    }

    /**
     * Creates a new instance of {@link ProductSalePrice}.
     *
     * @return - new instance
     */
    public ProductSalePrice build() {
      ProductSalePrice productSalePrice = new ProductSalePrice();
      productSalePrice.price(price);
      productSalePrice.profit(profit);
      return productSalePrice;
    }
  }
}
