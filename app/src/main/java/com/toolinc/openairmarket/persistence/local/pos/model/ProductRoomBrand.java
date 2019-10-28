package com.toolinc.openairmarket.persistence.local.pos.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;
import com.google.auto.value.AutoValue.CopyAnnotations;
import com.toolinc.openairmarket.common.persistence.room.model.RoomCatalogModel;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductBrand;

/** Define the different brand of a {@link ProductRoomManufacturer}. */
@Entity(
    tableName = "ProductBrand",
    indices = {
      @Index(
          name = "ProductBrand_UK",
          value = {"referenceId", "name"},
          unique = true)
    },
    foreignKeys =
        @ForeignKey(
            entity = ProductRoomManufacturer.class,
            parentColumns = "id",
            childColumns = "productManufacturerId",
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT))
@AutoValue
public abstract class ProductRoomBrand implements RoomCatalogModel {

  @CopyAnnotations
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  public abstract String id();

  @CopyAnnotations
  @Nullable
  @ColumnInfo(name = "productManufacturerId")
  public abstract String productManufacturerId();

  public static ProductRoomBrand create(
      boolean active,
      String referenceId,
      String name,
      String id,
      @Nullable String productManufacturerId) {
    return new AutoValue_ProductRoomBrand.Builder()
        .setActive(active)
        .setReferenceId(referenceId)
        .setName(name)
        .setId(id)
        .setProductManufacturerId(productManufacturerId)
        .build();
  }

  public abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_ProductRoomBrand.Builder();
  }

  /** Builder class for {@link ProductRoomBrand}. */
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setId(String id);

    public abstract Builder setReferenceId(String referenceId);

    public abstract Builder setName(String name);

    public abstract Builder setProductManufacturerId(@Nullable String productManufacturerId);

    public Builder setProductManufacturer(ProductRoomManufacturer productManufacturer) {
      return setProductManufacturerId(productManufacturer.id());
    }

    public abstract Builder setActive(boolean active);

    public Builder setProductBrand(ProductBrand productBrand) {
      return setActive(productBrand.getActive())
          .setReferenceId(productBrand.getReferenceId())
          .setName(productBrand.getName())
          .setProductManufacturerId(productBrand.getProductManufacturer())
          .setId(productBrand.id());
    }

    public abstract ProductRoomBrand build();
  }
}
