package com.toolinc.openairmarket.persistence.local.pos.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomCategory;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/** Specifies the data access object behavior for the {@link ProductRoomCategory}. */
@Dao
public interface ProductRoomCategoryDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  Completable insert(ProductRoomCategory productRoomCategory);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  Completable update(ProductRoomCategory productRoomCategory);

  @Delete
  Maybe<Integer> delete(ProductRoomCategory productRoomCategory);

  @Query("DELETE FROM ProductCategory")
  Completable deleteAll();

  @Query("SELECT * FROM ProductCategory WHERE rowid = :id")
  Maybe<ProductRoomCategory> findById(String id);
}
