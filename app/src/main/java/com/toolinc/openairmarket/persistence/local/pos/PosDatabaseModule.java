package com.toolinc.openairmarket.persistence.local.pos;

import android.app.Application;

import androidx.room.Room;

import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomCategoryDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/** Specifies the Pos database dagger module. */
@Module
public class PosDatabaseModule {

  private static volatile PosRoomDatabase posRoomDatabase;

  public PosDatabaseModule(Application mApplication) {
    if (posRoomDatabase == null) {
      synchronized (PosDatabaseModule.class) {
        if (posRoomDatabase == null) {
          posRoomDatabase =
              Room.databaseBuilder(mApplication, PosRoomDatabase.class, "pos_database").build();
        }
      }
    }
  }

  @Singleton
  @Provides
  PosRoomDatabase providesRoomDatabase() {
    return posRoomDatabase;
  }

  @Provides
  ProductRoomCategoryDao providesProductRoomCategoryDao(PosRoomDatabase posRoomDatabase) {
    return posRoomDatabase.productRoomCategoryDao();
  }
}
