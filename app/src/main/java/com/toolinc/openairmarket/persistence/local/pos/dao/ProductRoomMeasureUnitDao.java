package com.toolinc.openairmarket.persistence.local.pos.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomMeasureUnit;

import io.reactivex.Maybe;

/** Specifies the data access object behavior for the {@link ProductRoomMeasureUnitDao}. */
@Dao
public interface ProductRoomMeasureUnitDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(ProductRoomMeasureUnit productRoomMeasureUnit);

  @Delete
  void delete(ProductRoomMeasureUnit productRoomMeasureUnit);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void update(ProductRoomMeasureUnit productRoomMeasureUnit);

  @Query("SELECT * FROM ProductMeasureUnit ORDER BY id DESC")
  DataSource.Factory<Integer, ProductRoomMeasureUnit> all();

  @Query("SELECT * FROM ProductMeasureUnit WHERE id = :id")
  Maybe<ProductRoomMeasureUnit> findById(String id);

  @Query("SELECT * FROM ProductMeasureUnit WHERE referenceId = :referenceId")
  Maybe<ProductRoomMeasureUnit> findByReferenceId(String referenceId);

  @Query("SELECT * FROM ProductMeasureUnit WHERE name LIKE '%' || :name || '%'")
  DataSource.Factory<Integer, ProductRoomMeasureUnit> findAllLikeName(String name);
}
