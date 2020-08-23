package com.toolinc.openairmarket.persistence.local.pos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;
import com.google.auto.value.AutoValue.CopyAnnotations;
import com.toolinc.openairmarket.common.persistence.room.model.RoomCatalogModel;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductPurchasePrice;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductSalePrice;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductType;

/** Specifies the characteristics of a product. */
@Entity(
    tableName = "Product",
    indices = {
      @Index(
          name = "Product_UK",
          value = {"referenceId", "name"},
          unique = true),
      @Index(name = "ProductCategory_FK", value = "productCategoryId"),
      @Index(name = "ProductMeasureUnit_FK", value = "productMeasureUnitId"),
      @Index(name = "ProductBrand_FK", value = "productBrandId")
    },
    foreignKeys = {
      @ForeignKey(
          entity = ProductRoomCategory.class,
          parentColumns = "id",
          childColumns = "productCategoryId",
          onUpdate = ForeignKey.CASCADE,
          onDelete = ForeignKey.CASCADE),
      @ForeignKey(
          entity = ProductRoomMeasureUnit.class,
          parentColumns = "id",
          childColumns = "productMeasureUnitId",
          onUpdate = ForeignKey.CASCADE,
          onDelete = ForeignKey.CASCADE),
      @ForeignKey(
          entity = ProductRoomBrand.class,
          parentColumns = "id",
          childColumns = "productBrandId",
          onUpdate = ForeignKey.CASCADE,
          onDelete = ForeignKey.CASCADE)
    })
@AutoValue
public abstract class ProductRoom implements RoomCatalogModel {

  @CopyAnnotations
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  public abstract String id();

  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "image")
  public abstract String image();

  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "productType")
  public abstract ProductType productType();

  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "productCategoryId")
  public abstract String productCategory();

  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "productMeasureUnitId")
  public abstract String productMeasureUnit();

  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "productBrandId")
  public abstract String productBrand();

  @CopyAnnotations
  @NonNull
  @Embedded
  public abstract ProductRoomSalePrice productSalePrice();

  @CopyAnnotations
  @NonNull
  @Embedded
  public abstract ProductRoomPurchasePrice productPurchasePrice();

  public static ProductRoom create(
      boolean active,
      String referenceId,
      String name,
      String id,
      String image,
      ProductType productType,
      String productCategory,
      String productMeasureUnit,
      String productBrand,
      ProductRoomSalePrice productSalePrice,
      ProductRoomPurchasePrice productPurchasePrice) {
    return builder()
        .setActive(active)
        .setReferenceId(referenceId)
        .setName(name)
        .setId(id)
        .setImage(image)
        .setProductType(productType)
        .setProductCategory(productCategory)
        .setProductMeasureUnit(productMeasureUnit)
        .setProductBrand(productBrand)
        .setProductSalePrice(productSalePrice)
        .setProductPurchasePrice(productPurchasePrice)
        .build();
  }

  public abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_ProductRoom.Builder();
  }

  /** Builder class for {@link ProductRoom}. */
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setId(String id);

    public abstract Builder setReferenceId(String referenceId);

    public abstract Builder setName(String name);

    public abstract Builder setImage(String image);

    public abstract Builder setProductType(ProductType productType);

    public abstract Builder setProductCategory(String productCategory);

    public abstract Builder setProductMeasureUnit(String productMeasureUnit);

    public abstract Builder setProductBrand(String productBrand);

    public abstract Builder setProductSalePrice(ProductRoomSalePrice productSalePrice);

    public Builder setProductSalePrice(ProductSalePrice productSalePrice) {
      return setProductSalePrice(
          ProductRoomSalePrice.create(
              productSalePrice.listPrice(),
              productSalePrice.price(),
              productSalePrice.limitPrice(),
              productSalePrice.profit()));
    }

    public abstract Builder setProductPurchasePrice(ProductRoomPurchasePrice productPurchasePrice);

    public Builder setProductPurchasePrice(ProductPurchasePrice productPurchasePrice) {
      return setProductPurchasePrice(
          ProductRoomPurchasePrice.create(
              productPurchasePrice.listPrice(),
              productPurchasePrice.price(),
              productPurchasePrice.limitPrice()));
    }

    public abstract Builder setActive(boolean active);

    public Builder setProduct(Product product) {
      return setActive(product.getActive())
          .setReferenceId(product.getReferenceId())
          .setName(product.getName())
          .setId(product.id())
          .setImage(product.getImage())
          .setProductType(product.getProductType())
          .setProductCategory(product.getProductCategory())
          .setProductMeasureUnit(product.getProductMeasureUnit())
          .setProductBrand(product.getProductBrand())
          .setProductSalePrice(product.getProductSalePrice())
          .setProductPurchasePrice(product.getProductPurchasePrice());
    }

    public abstract ProductRoom build();
  }
}
