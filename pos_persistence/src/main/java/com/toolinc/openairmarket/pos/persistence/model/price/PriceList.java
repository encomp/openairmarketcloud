package com.toolinc.openairmarket.pos.persistence.model.price;

import com.google.common.base.Preconditions;
import com.toolinc.openairmarket.common.persistence.model.AbstractActiveReferenceModel;

/**
 * Price lists determine currency of the document as well as tax treatment. The price list is a
 * further refinement of the price list schema which you can apply to particular situations or
 * markets.
 */
public abstract class PriceList extends AbstractActiveReferenceModel {

  private String description;

  private String currency;

  private Boolean defaulted;

  private Boolean taxIncluded;

  public abstract PriceListType getPriceListType();

  public abstract void setPriceListType(PriceListType priceListType);

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = checkNillable(description);
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = checkNotEmpty(currency);
  }

  public Boolean getDefaulted() {
    return defaulted;
  }

  public void setDefaulted(Boolean defaulted) {
    this.defaulted = Preconditions.checkNotNull(defaulted);
  }

  public Boolean getTaxIncluded() {
    return taxIncluded;
  }

  public void setTaxIncluded(Boolean taxIncluded) {
    this.taxIncluded = Preconditions.checkNotNull(taxIncluded);
  }
}
