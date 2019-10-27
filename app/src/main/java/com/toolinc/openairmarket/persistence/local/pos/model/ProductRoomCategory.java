package com.toolinc.openairmarket.persistence.local.pos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;

import com.google.auto.value.AutoValue;
import com.google.auto.value.AutoValue.CopyAnnotations;
import com.toolinc.openairmarket.common.persistence.room.model.RoomCatalogModel;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductCategory;

/** Define the different categories in which a {@link Product} can be assigned. */
@Fts4
@Entity(tableName = "ProductCategory")
@AutoValue
public abstract class ProductRoomCategory implements RoomCatalogModel {

  @CopyAnnotations
  @NonNull
  @ColumnInfo(name = "id")
  public abstract String id();

  public static ProductRoomCategory create(
      boolean active, String referenceId, String name, String id) {
    return new AutoValue_ProductRoomCategory.Builder()
        .setActive(active)
        .setReferenceId(referenceId)
        .setName(name)
        .setId(id)
        .build();
  }

  public abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_ProductRoomCategory.Builder();
  }

  /** Builder class for {@link ProductRoomCategory}. */
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setId(String id);

    public abstract Builder setReferenceId(String referenceId);

    public abstract Builder setName(String name);

    public abstract Builder setActive(boolean active);

    public Builder setProductCategory(ProductCategory productCategory) {
      return setActive(productCategory.getActive())
          .setReferenceId(productCategory.getReferenceId())
          .setName(productCategory.getName())
          .setId(productCategory.id());
    }

    public abstract ProductRoomCategory build();
  }
}
