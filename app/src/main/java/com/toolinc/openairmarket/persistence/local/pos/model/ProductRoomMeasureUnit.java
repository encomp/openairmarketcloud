package com.toolinc.openairmarket.persistence.local.pos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;
import com.google.auto.value.AutoValue.CopyAnnotations;
import com.toolinc.openairmarket.common.persistence.room.model.RoomCatalogModel;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductMeasureUnit;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductType;

/** Define the different measure units of a {@link ProductType}. */
@Entity(
    tableName = "ProductMeasureUnit",
    indices = {
      @Index(
          name = "ProductMeasureUnit_UK",
          value = {"referenceId", "name"},
          unique = true)
    })
@AutoValue
public abstract class ProductRoomMeasureUnit implements RoomCatalogModel {

  @CopyAnnotations
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  public abstract String id();

  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "countable")
  public abstract boolean countable();

  public abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_ProductRoomMeasureUnit.Builder();
  }

  public static ProductRoomMeasureUnit create(
      boolean active, String referenceId, String name, String id, boolean countable) {
    return new AutoValue_ProductRoomMeasureUnit.Builder()
        .setActive(active)
        .setReferenceId(referenceId)
        .setName(name)
        .setId(id)
        .setCountable(countable)
        .build();
  }

  /** Builder class for {@link ProductRoomMeasureUnit}. */
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setId(String id);

    public abstract Builder setReferenceId(String referenceId);

    public abstract Builder setName(String name);

    public abstract Builder setCountable(boolean countable);

    public abstract Builder setActive(boolean active);

    public Builder setProductMeasureUnit(ProductMeasureUnit productMeasureUnit) {
      return setActive(productMeasureUnit.getActive())
          .setReferenceId(productMeasureUnit.getReferenceId())
          .setName(productMeasureUnit.getName())
          .setCountable(productMeasureUnit.getCountable())
          .setId(productMeasureUnit.id());
    }

    public abstract ProductRoomMeasureUnit build();
  }
}
