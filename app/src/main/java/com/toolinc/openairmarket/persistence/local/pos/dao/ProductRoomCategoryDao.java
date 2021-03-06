package com.toolinc.openairmarket.persistence.local.pos.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomCategory;

import io.reactivex.Maybe;

/** Specifies the data access object behavior for the {@link ProductRoomCategory}. */
@Dao
public interface ProductRoomCategoryDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(ProductRoomCategory productRoomCategory);

  @Delete
  void delete(ProductRoomCategory productRoomCategory);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void update(ProductRoomCategory productRoomCategory);

  @Query("SELECT * FROM ProductCategory ORDER BY id DESC")
  DataSource.Factory<Integer, ProductRoomCategory> all();

  @Query("SELECT * FROM ProductCategory WHERE id = :id")
  Maybe<ProductRoomCategory> findById(String id);

  @Query("SELECT * FROM ProductCategory WHERE referenceId = :referenceId")
  Maybe<ProductRoomCategory> findByReferenceId(String referenceId);

  @Query("SELECT * FROM ProductCategory WHERE name LIKE '%' || :name || '%'")
  DataSource.Factory<Integer, ProductRoomCategory> findAllLikeName(String name);
}
