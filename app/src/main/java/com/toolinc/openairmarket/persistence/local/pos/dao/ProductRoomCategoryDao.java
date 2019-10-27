package com.toolinc.openairmarket.persistence.local.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomCategory;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/** Specifies the data access object behavior for the {@link ProductRoomCategory}. */
@Dao
public interface ProductRoomCategoryDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  Completable insert(ProductRoomCategory productRoomCategory);

  @Query("DELETE FROM ProductCategory WHERE id = :id")
  Completable delete(String id);

  @Query("SELECT * FROM ProductCategory WHERE id = :id")
  Maybe<ProductRoomCategory> findById(String id);
}
