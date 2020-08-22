package com.toolinc.openairmarket.persistence.local.offline;

import android.content.Context;

import androidx.room.Room;

import com.toolinc.openairmarket.common.inject.Global;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

/** Specifies the Offline database dagger module. */
@InstallIn(ApplicationComponent.class)
@Module
public class OfflineDatabaseModule {

  @Singleton
  @Provides
  OfflineRoomDatabase providesRoomDatabase(@ApplicationContext Context appContext) {
    return Room.databaseBuilder(appContext, OfflineRoomDatabase.class, "offline_database").build();
  }

  @Provides
  CollectionSyncStateDao providesCollectionStateDao(OfflineRoomDatabase demoDatabase) {
    return demoDatabase.collectionStateDao();
  }

  @Provides
  CollectionSyncStateRepository providesCollectionStateRepository(
      CollectionSyncStateDao collectionSyncStateDao) {
    return new CollectionSyncStateRepository(collectionSyncStateDao);
  }
}
