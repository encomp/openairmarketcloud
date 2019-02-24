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
    if (listPrice != null) {
      this.listPrice = checkPositive(new BigDecimal(listPrice));
    }
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
    if (limitPrice != null) {
      this.limitPrice = checkPositive(new BigDecimal(limitPrice));
    }
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

  // TODO: Remove the positive check due to data migration information.
  public void setProfit(String profit) {
    this.profit = new BigDecimal(profit);
  }

  public BigDecimal profit() {
    return this.profit;
  }

  // TODO: Remove the positive check due to data migration information.
  public void profit(BigDecimal profit) {
    this.profit = profit;
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

    public Builder setPrice(String price) {
      BigDecimal bigDecimal = new BigDecimal(price.trim());
      return setPrice(bigDecimal);
    }

    public Builder setPrice(BigDecimal price) {
      this.price = checkPositive(price);
      return this;
    }

    public Builder setProfit(String profit) {
      BigDecimal bigDecimal = new BigDecimal(profit.trim());
      return setProfit(bigDecimal);
    }

    // TODO: Remove the positive check due to data migration information.
    public Builder setProfit(BigDecimal profit) {
      this.profit = profit;
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
