package com.toolinc.openairmarket.persistence.local.pos.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomManufacturer;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/** Specifies the data access object behavior for the {@link ProductRoomManufacturer}. */
@Dao
public interface ProductRoomManufacturerDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  Completable insert(ProductRoomManufacturer productRoomManufacturer);

  @Delete
  Completable delete(ProductRoomManufacturer productRoomManufacturer);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  Completable update(ProductRoomManufacturer productRoomManufacturer);

  @Query("SELECT * FROM ProductManufacturer ORDER BY id DESC")
  DataSource.Factory<Integer, ProductRoomManufacturer> all();

  @Query("SELECT * FROM ProductManufacturer WHERE id = :id")
  Maybe<ProductRoomManufacturer> findById(String id);

  @Query("SELECT * FROM ProductManufacturer WHERE referenceId = :referenceId")
  Maybe<ProductRoomManufacturer> findByReferenceId(String referenceId);

  @Query("SELECT * FROM ProductManufacturer WHERE name LIKE '%' || :name || '%'")
  DataSource.Factory<Integer, ProductRoomManufacturer> findAllLikeName(String name);
}
