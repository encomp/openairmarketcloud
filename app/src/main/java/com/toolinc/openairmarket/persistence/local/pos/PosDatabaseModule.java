package com.toolinc.openairmarket.persistence.local.pos;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomBrandDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomCategoryDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomManufacturerDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomMeasureUnitDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

/** Specifies the Pos database dagger module. */
@InstallIn(ApplicationComponent.class)
@Module
public class PosDatabaseModule {

  @Singleton
  @Provides
  PosRoomDatabase providesRoomDatabase(@ApplicationContext Context appContext) {
    return Room.databaseBuilder(appContext, PosRoomDatabase.class, "pos_database").build();
  }

  @Provides
  ProductRoomBrandDao providesProductRoomBrandDao(PosRoomDatabase posRoomDatabase) {
    return posRoomDatabase.productRoomBrandDao();
  }

  @Provides
  ProductRoomCategoryDao providesProductRoomCategoryDao(PosRoomDatabase posRoomDatabase) {
    return posRoomDatabase.productRoomCategoryDao();
  }

  @Provides
  ProductRoomDao providesProductRoomDao(PosRoomDatabase posRoomDatabase) {
    return posRoomDatabase.productRoomDao();
  }

  @Provides
  ProductRoomManufacturerDao providesProductRoomManufacturerDao(PosRoomDatabase posRoomDatabase) {
    return posRoomDatabase.productRoomManufacturerDao();
  }

  @Provides
  ProductRoomMeasureUnitDao providesProductRoomMeasureUnitDao(PosRoomDatabase posRoomDatabase) {
    return posRoomDatabase.productRoomMeasureUnitDao();
  }
}
