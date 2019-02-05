package com.toolinc.openairmarket.pos.persistence.model.product;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.persistence.model.AbstractCatalogModel;

/** Specifies the characteristics of a product. */
public final class Product extends AbstractCatalogModel {

  private String id;

  private String image;

  private ProductType productType;

  private String productCategory;

  private String productMeasureUnit;

  private String productBrand;

  private ProductSalePrice productSalePrice;

  private ProductPurchasePrice productPurchasePrice;

  public String id() {
    return id;
  }

  public void setId(String id) {
    this.id = checkNotEmpty(id);
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = Preconditions.checkNotNull(productType);
  }

  public String getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(String productCategory) {
    this.productCategory = checkNotEmpty(productCategory);
  }

  public String getProductMeasureUnit() {
    return productMeasureUnit;
  }

  public void setProductMeasureUnit(String productMeasureUnit) {
    this.productMeasureUnit = checkNotEmpty(productMeasureUnit);
  }

  public String getProductBrand() {
    return productBrand;
  }

  public void setProductBrand(String productBrand) {
    this.productBrand = checkNotEmpty(productBrand);
  }

  public ProductSalePrice getProductSalePrice() {
    return productSalePrice;
  }

  public void setProductSalePrice(ProductSalePrice productSalePrice) {
    this.productSalePrice = Preconditions.checkNotNull(productSalePrice);
  }

  public ProductPurchasePrice getProductPurchasePrice() {
    return productPurchasePrice;
  }

  public void setProductPurchasePrice(ProductPurchasePrice productPurchasePrice) {
    this.productPurchasePrice = Preconditions.checkNotNull(productPurchasePrice);
  }

  /**
   * Creates a new {@link Builder} instance.
   *
   * @return - new instance
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /** Builder class that creates instances of {@link Product}. */
  public static final class Builder implements Domain {

    private String referenceId;
    private String name;
    private String image;
    private ProductType productType;
    private String productCategory;
    private String productMeasureUnit;
    private String productBrand;
    private ProductSalePrice productSalePrice;
    private ProductPurchasePrice productPurchasePrice;

    public Builder setReferenceId(String referenceId) {
      this.referenceId = checkNotEmpty(referenceId).toUpperCase();
      return this;
    }

    public Builder setName(String name) {
      this.name = checkNotEmpty(name).toUpperCase();
      return this;
    }

    public Builder setImage(String image) {
      this.image = image;
      return this;
    }

    public Builder setProductType(ProductType productType) {
      this.productType = Preconditions.checkNotNull(productType);
      return this;
    }

    public Builder setProductCategory(String productCategory) {
      this.productCategory = checkNotEmpty(productCategory);
      return this;
    }

    public Builder setProductMeasureUnit(String productMeasureUnit) {
      this.productMeasureUnit = checkNotEmpty(productMeasureUnit);
      return this;
    }

    public Builder setProductBrand(String productBrand) {
      this.productBrand = checkNotEmpty(productBrand);
      return this;
    }

    public Builder setProductSalePrice(ProductSalePrice productSalePrice) {
      this.productSalePrice = Preconditions.checkNotNull(productSalePrice);
      return this;
    }

    public Builder setProductPurchasePrice(ProductPurchasePrice productPurchasePrice) {
      this.productPurchasePrice = Preconditions.checkNotNull(productPurchasePrice);
      return this;
    }

    /**
     * Creates a new instance of {@link Product}.
     *
     * @return - new instance
     */
    public Product build() {
      Product productDefinition = new Product();
      if (!Strings.isNullOrEmpty(referenceId)) {
        productDefinition.setReferenceId(referenceId);
      }
      productDefinition.setName(name);
      productDefinition.setImage(image);
      productDefinition.setProductType(productType);
      productDefinition.setProductCategory(productCategory);
      productDefinition.setProductMeasureUnit(productMeasureUnit);
      productDefinition.setProductBrand(productBrand);
      productDefinition.setProductSalePrice(productSalePrice);
      productDefinition.setProductPurchasePrice(productPurchasePrice);
      return productDefinition;
    }
  }
}
