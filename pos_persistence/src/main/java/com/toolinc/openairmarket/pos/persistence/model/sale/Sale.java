package com.toolinc.openairmarket.pos.persistence.model.sale;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.persistence.model.AbstractModel;
import com.toolinc.openairmarket.pos.persistence.model.PaymentMethod;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/** Specifies the sale information. */
public class Sale extends AbstractModel {

  private String id;
  private SaleType saleType;
  private PaymentMethod paymentMethod;
  private DateTime date;
  private BigDecimal amount;
  private BigDecimal tax;
  private BigDecimal total;
  private String systemUser;
  private ImmutableList<SaleLine> saleLines;

  public String id() {
    return id;
  }

  public void setId(String id) {
    this.id = checkNotEmpty(id);
  }

  public SaleType getSaleType() {
    return saleType;
  }

  public void setSaleType(SaleType saleType) {
    this.saleType = checkNotNull(saleType);
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = checkNotNull(paymentMethod);
  }

  public DateTime date() {
    return date;
  }

  public void date(DateTime dateTime) {
    this.date = toDateTime(dateTime);
  }

  public Date getDate() {
    return toDate(date);
  }

  public void setDate(Date date) {
    this.date = toDateTime(date);
  }

  public BigDecimal amount() {
    return amount;
  }

  public void amount(BigDecimal amount) {
    this.amount = checkPositive(amount);
  }

  public String getAmount() {
    return toStringMoney(amount);
  }

  public void setAmount(String amount) {
    this.amount = checkPositive(new BigDecimal(amount));
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

  public String getSystemUser() {
    return systemUser;
  }

  public void setSystemUser(String systemUser) {
    this.systemUser = checkNotEmpty(systemUser);
  }

  public ImmutableList<SaleLine> saleLines() {
    return saleLines;
  }

  public void saleLines(ImmutableList<SaleLine> saleLines) {
    checkNotNull(saleLines);
    checkArgument(!saleLines.isEmpty());
    this.saleLines = saleLines;
  }

  public List<SaleLine> getSaleLines() {
    return saleLines;
  }

  public void setSaleLines(List<SaleLine> saleLines) {
    this.saleLines = ImmutableList.copyOf(saleLines);
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

    private String id;
    private SaleType saleType;
    private PaymentMethod paymentMethod;
    private DateTime date;
    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal total;
    private String systemUser;
    private ImmutableList<SaleLine> saleLines;

    private Builder() {}

    public Builder setId(String id) {
      this.id = checkNotEmpty(id);
      return this;
    }

    public Builder setSaleType(SaleType saleType) {
      this.saleType = saleType;
      return this;
    }

    public Builder setPaymentMethod(PaymentMethod paymentMethod) {
      this.paymentMethod = paymentMethod;
      return this;
    }

    public Builder setDate(DateTime dateTime) {
      this.date = dateTime;
      return this;
    }

    public Builder setAmount(BigDecimal amount) {
      this.amount = amount;
      return this;
    }

    public Builder setTax(BigDecimal tax) {
      this.tax = tax;
      return this;
    }

    public Builder total(BigDecimal total) {
      this.total = total;
      return this;
    }

    public Builder setSystemUser(String systemUser) {
      this.systemUser = systemUser;
      return this;
    }

    public Builder setSaleLines(ImmutableList<SaleLine> saleLines) {
      this.saleLines = saleLines;
      return this;
    }

    /**
     * Creates a new instance of {@link Sale}.
     *
     * @return - new instance
     */
    public Sale build() {
      Sale sale = new Sale();
      if (!Strings.isNullOrEmpty(id)) {
        sale.setId(id);
      }
      sale.setSaleType(saleType);
      sale.setPaymentMethod(paymentMethod);
      sale.date(date);
      sale.amount(amount);
      if (tax != null && tax.doubleValue() > 0.0) {
        sale.tax(tax);
      }
      sale.total(total);
      sale.setSystemUser(systemUser);
      sale.saleLines(saleLines);
      return sale;
    }
  }
}
