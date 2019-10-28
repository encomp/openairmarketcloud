package com.toolinc.openairmarket.persistence.local.pos;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomBrandDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomCategoryDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomManufacturerDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomMeasureUnitDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomBrand;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomCategory;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomManufacturer;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomMeasureUnit;

/** Provides direct access to the underlying POS database. */
@Database(
    entities = {
      ProductRoomBrand.class,
      ProductRoomCategory.class,
      ProductRoomManufacturer.class,
      ProductRoomMeasureUnit.class
    },
    exportSchema = false,
    version = 1)
abstract class PosRoomDatabase extends RoomDatabase {

  public abstract ProductRoomBrandDao productRoomBrandDao();

  public abstract ProductRoomCategoryDao productRoomCategoryDao();

  public abstract ProductRoomManufacturerDao productRoomManufacturerDao();

  public abstract ProductRoomMeasureUnitDao productRoomMeasureUnitDao();
}
