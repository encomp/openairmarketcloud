package com.toolinc.openairmarket.pos.persistence.model.price;

import com.google.common.base.Preconditions;
import com.toolinc.openairmarket.common.persistence.model.AbstractActiveReferenceModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The price schema is the overall system of pricing for your organization and can specify discounts
 * for products, product categories and business partners. It calculates the trade discount
 * percentage.
 */
public final class PriceListSchema extends AbstractActiveReferenceModel {

  private String description;

  private Date validFrom;

  private Boolean quantityBased;

  private DiscountType discountType;

  private BigDecimal flatDiscount;

  private String discountFormula;

  private PriceType priceType;

  private RoundingType roundingType;

  private BigDecimal basePrice;

  private BigDecimal surchargeAmount;

  private BigDecimal discountAmount;

  private BigDecimal minimumPriceMargin;

  private BigDecimal maximumPriceMargin;

  private BigDecimal fixedPrice;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = checkNillable(description);
  }

  public Date getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(Date validFrom) {
    this.validFrom = Preconditions.checkNotNull(validFrom);
  }

  public Boolean getQuantityBased() {
    return quantityBased;
  }

  public void setQuantityBased(Boolean quantityBased) {
    this.quantityBased = Preconditions.checkNotNull(quantityBased);
  }

  public DiscountType getDiscountType() {
    return discountType;
  }

  public void setDiscountType(DiscountType discountType) {
    this.discountType = Preconditions.checkNotNull(discountType);
  }

  public BigDecimal getFlatDiscount() {
    return flatDiscount;
  }

  public void setFlatDiscount(BigDecimal flatDiscount) {
    this.flatDiscount = checkNillablePositive(flatDiscount);
  }

  public String getDiscountFormula() {
    return discountFormula;
  }

  public void setDiscountFormula(String discountFormula) {
    this.discountFormula = discountFormula;
  }

  public PriceType getPriceType() {
    return priceType;
  }

  public void setPriceType(PriceType priceType) {
    this.priceType = Preconditions.checkNotNull(priceType);
  }

  /** Indicates how the final list price will be rounded. */
  public RoundingType getRoundingType() {
    return roundingType;
  }

  public void setRoundingType(RoundingType roundingType) {
    this.roundingType = Preconditions.checkNotNull(roundingType);
  }

  /**
   * The List Price Base indicates the price to use as the basis for the calculation of a new price
   * list.
   */
  public BigDecimal getBasePrice() {
    return basePrice;
  }

  public void setBasePrice(BigDecimal basePrice) {
    this.basePrice = checkPositive(basePrice);
  }

  /**
   * The List Price Surcharge Amount indicates the amount to be added to the price prior to
   * multiplication.
   */
  public BigDecimal getSurchargeAmount() {
    return surchargeAmount;
  }

  public void setSurchargeAmount(BigDecimal surchargeAmount) {
    this.surchargeAmount = checkPositive(surchargeAmount);
  }

  /**
   * The List Price Discount Percentage indicates the percentage discount which will be subtracted
   * from the base price. A negative amount indicates the percentage which will be added to the base
   * price.
   */
  public BigDecimal getDiscountAmount() {
    return discountAmount;
  }

  public void setDiscountAmount(BigDecimal discountAmount) {
    this.discountAmount = checkPositive(discountAmount);
  }

  /**
   * Indicates the minimum margin for a product. The margin is calculated by subtracting the
   * original limit price from the newly calculated price. If this field contains 0.00 then it is
   * ignored.
   */
  public BigDecimal getMinimumPriceMargin() {
    return minimumPriceMargin;
  }

  public void setMinimumPriceMargin(BigDecimal minimumPriceMargin) {
    this.minimumPriceMargin = checkPositive(minimumPriceMargin);
  }

  /**
   * Indicates the maximum margin for a product. The margin is calculated by subtracting the
   * original limit price from the newly calculated price. If this field contains 0.00 then it is
   * ignored.
   */
  public BigDecimal getMaximumPriceMargin() {
    return maximumPriceMargin;
  }

  public void setMaximumPriceMargin(BigDecimal maximumPriceMargin) {
    this.maximumPriceMargin = checkPositive(maximumPriceMargin);
  }

  /** Fixes List Price (not calculated). */
  public BigDecimal getFixedPrice() {
    return fixedPrice;
  }

  public void setFixedPrice(BigDecimal fixedPrice) {
    this.fixedPrice = checkPositive(fixedPrice);
  }
}
