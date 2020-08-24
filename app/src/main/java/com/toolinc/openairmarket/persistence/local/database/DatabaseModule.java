package com.toolinc.openairmarket.persistence.local.database;

import android.content.Context;

import androidx.room.Room;

import com.toolinc.openairmarket.persistence.local.database.dao.CollectionSyncStateDao;
import com.toolinc.openairmarket.persistence.local.database.dao.CollectionSyncStateRepository;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

/** Specifies the OpenAirMarket database dagger module. */
@InstallIn(ApplicationComponent.class)
@Module
public class DatabaseModule {

  @Singleton
  @Provides
  LocalRoomDatabase providesRoomDatabase(@ApplicationContext Context appContext) {
    return Room.databaseBuilder(appContext, LocalRoomDatabase.class, "open_air_market_database").build();
  }

  @Provides
  CollectionSyncStateDao providesCollectionStateDao(LocalRoomDatabase demoDatabase) {
    return demoDatabase.collectionStateDao();
  }

  @Provides
  CollectionSyncStateRepository providesCollectionStateRepository(
      CollectionSyncStateDao collectionSyncStateDao) {
    return new CollectionSyncStateRepository(collectionSyncStateDao);
  }
}
