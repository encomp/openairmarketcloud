package com.toolinc.openairmarket.persistence.local.pos.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomBrand;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/** Specifies the data access object behavior for the {@link ProductRoomBrand}. */
@Dao
public interface ProductRoomBrandDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  Completable insert(ProductRoomBrand productRoomCategory);

  @Delete
  Completable delete(ProductRoomBrand productRoomCategory);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  Completable update(ProductRoomBrand productRoomCategory);

  @Query("SELECT * FROM ProductBrand ORDER BY id DESC")
  DataSource.Factory<Integer, ProductRoomBrand> all();

  @Query("SELECT * FROM ProductBrand WHERE id = :id")
  Maybe<ProductRoomBrand> findById(String id);

  @Query("SELECT * FROM ProductBrand WHERE referenceId = :referenceId")
  Maybe<ProductRoomBrand> findByReferenceId(String referenceId);

  @Query("SELECT * FROM ProductBrand WHERE name LIKE '%' || :name || '%'")
  DataSource.Factory<Integer, ProductRoomBrand> findAllLikeName(String name);
}
