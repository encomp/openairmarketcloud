package com.toolinc.openairmarket.persistence.local.pos.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoom;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/** Specifies the data access object behavior for the {@link ProductRoom}. */
@Dao
public interface ProductRoomDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  Completable insert(ProductRoom productRoom);

  @Delete
  Completable delete(ProductRoom productRoom);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  Completable update(ProductRoom productRoom);

  @Query("SELECT * FROM Product ORDER BY id DESC")
  DataSource.Factory<Integer, ProductRoom> all();

  @Query("SELECT * FROM Product WHERE id = :id")
  Maybe<ProductRoom> findById(String id);

  @Query("SELECT * FROM Product WHERE referenceId = :referenceId")
  Maybe<ProductRoom> findByReferenceId(String referenceId);

  @Query("SELECT * FROM Product WHERE name LIKE '%' || :name || '%'")
  DataSource.Factory<Integer, ProductRoom> findAllLikeName(String name);
}
