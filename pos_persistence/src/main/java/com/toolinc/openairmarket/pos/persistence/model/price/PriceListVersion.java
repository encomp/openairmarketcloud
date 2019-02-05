package com.toolinc.openairmarket.pos.persistence.model.price;

import com.google.common.base.Preconditions;
import com.toolinc.openairmarket.common.persistence.model.AbstractActiveReferenceModel;

import java.util.Date;

/**
 * The price list version is a specific version of an individual price list. Working with price list
 * versions enables you to create even more specific price lists, for example for specific
 * customers, or for a limited time period such as an end-of-season sale. Price list versions are
 * time-bound so remain valid only within the dates you specify.
 *
 * <p>Price lists are automatically created based on Product Purchasing Information and the Vendor
 * Category Discount. The other alternative is to copy them from existing price lists and then
 * re-calculate them.
 */
public abstract class PriceListVersion extends AbstractActiveReferenceModel {

  private PriceListSchema priceListSchema;

  private Integer seqNo;

  private Date validFrom;

  private String description;

  public PriceListSchema getPriceListSchema() {
    return priceListSchema;
  }

  public void setPriceListSchema(PriceListSchema priceListSchema) {
    this.priceListSchema = Preconditions.checkNotNull(priceListSchema);
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = checkPositive(seqNo);
  }

  public Date getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(Date validFrom) {
    this.validFrom = Preconditions.checkNotNull(validFrom);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = checkNillable(description);
  }
}
