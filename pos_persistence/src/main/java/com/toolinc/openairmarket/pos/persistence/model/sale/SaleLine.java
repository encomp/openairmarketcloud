package com.toolinc.openairmarket.pos.persistence.model.sale;

import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.persistence.model.AbstractModel;

import java.math.BigDecimal;

/** Defines a line of an entire sale the product and its amount. */
public class SaleLine extends AbstractModel {

  private String product;
  private int lineOrder;
  private BigDecimal quantity;
  private BigDecimal price;
  private BigDecimal tax;
  private BigDecimal total;

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public int getLineOrder() {
    return lineOrder;
  }

  public void setLineOrder(int lineOrder) {
    this.lineOrder = lineOrder;
  }

  public BigDecimal quantity() {
    return quantity;
  }

  public void quantity(BigDecimal quantity) {
    this.quantity = checkPositive(quantity);
  }

  public String getQuantity() {
    return toStringMoney(quantity);
  }

  public void setQuantity(String quantity) {
    this.quantity = checkPositive(new BigDecimal(quantity));
  }

  public BigDecimal price() {
    return price;
  }

  public void price(BigDecimal price) {
    this.price = checkPositive(price);
  }

  public String getPrice() {
    return toStringMoney(price);
  }

  public void setPrice(String price) {
    this.price = checkPositive(new BigDecimal(price));
  }

  public BigDecimal tax() {
    return tax;
  }

  public void tax(BigDecimal tax) {
    this.tax = checkPositive(tax);
  }

  public String getTax() {
    return toStringMoney(tax);
  }

  public void setTax(String tax) {
    if (tax != null) {
      this.tax = checkPositive(new BigDecimal(tax));
    }
  }

  public BigDecimal total() {
    return total;
  }

  public void total(BigDecimal total) {
    this.total = checkPositive(total);
  }

  public String getTotal() {
    return toStringMoney(total);
  }

  public void setTotal(String total) {
    this.total = checkPositive(new BigDecimal(total));
  }

  /**
   * Creates a new {@link Builder} instance.
   *
   * @return - new instance
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /** Builder class that creates instances of {@link Sale}. */
  public static final class Builder implements Domain {

    private String product;
    private int lineOrder;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal tax;
    private BigDecimal total;

    private Builder() {}

    public Builder setProduct(String product) {
      this.product = product;
      return this;
    }

    public Builder setLineOrder(int lineOrder) {
      this.lineOrder = lineOrder;
      return this;
    }

    public Builder setQuantity(BigDecimal quantity) {
      this.quantity = quantity;
      return this;
    }

    public Builder setPrice(BigDecimal price) {
      this.price = price;
      return this;
    }

    public Builder setTax(BigDecimal tax) {
      this.tax = tax;
      return this;
    }

    public Builder setTotal(BigDecimal total) {
      this.total = total;
      return this;
    }

    /**
     * Creates a new instance of {@link SaleLine}.
     *
     * @return - new instance
     */
    public SaleLine build() {
      SaleLine saleLine = new SaleLine();
      saleLine.setProduct(product);
      saleLine.setLineOrder(lineOrder);
      saleLine.quantity(quantity);
      saleLine.price(price);
      saleLine.tax(tax);
      saleLine.total(total);
      return saleLine;
    }
  }
}
