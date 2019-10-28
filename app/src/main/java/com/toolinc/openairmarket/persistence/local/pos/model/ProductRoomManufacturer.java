package com.toolinc.openairmarket.persistence.local.pos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;
import com.toolinc.openairmarket.common.persistence.room.model.RoomCatalogModel;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductManufacturer;

/** Define the different companies that produces products. */
@Entity(
    tableName = "ProductManufacturer",
    indices = {
      @Index(
          name = "ProductManufacturer_UK",
          value = {"referenceId", "name"},
          unique = true)
    })
@AutoValue
public abstract class ProductRoomManufacturer implements RoomCatalogModel {

  @AutoValue.CopyAnnotations
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  public abstract String id();

  public static ProductRoomManufacturer create(
      boolean active, String referenceId, String name, String id) {
    return new AutoValue_ProductRoomManufacturer.Builder()
        .setActive(active)
        .setReferenceId(referenceId)
        .setName(name)
        .setId(id)
        .build();
  }

  public abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_ProductRoomManufacturer.Builder();
  }

  /** Builder class for {@link ProductRoomManufacturer}. */
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setId(String id);

    public abstract Builder setReferenceId(String referenceId);

    public abstract Builder setName(String name);

    public abstract Builder setActive(boolean active);

    public Builder setProductManufacturer(ProductManufacturer productManufacturer) {
      return setActive(productManufacturer.getActive())
          .setReferenceId(productManufacturer.getReferenceId())
          .setName(productManufacturer.getName())
          .setId(productManufacturer.id());
    }

    public abstract ProductRoomManufacturer build();
  }
}
