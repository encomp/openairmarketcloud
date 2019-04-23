package com.toolinc.openairmarket.persistence.local.offline;

import android.app.Application;

import androidx.room.Room;

import com.toolinc.openairmarket.common.inject.Global;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/** Specifies the Offline database dagger module. */
@Module
public class OfflineDatabaseModule {

  private static volatile OfflineRoomDatabase offlineRoomDatabase;

  public OfflineDatabaseModule(Application mApplication) {
    if (offlineRoomDatabase == null) {
      synchronized (OfflineDatabaseModule.class) {
        if (offlineRoomDatabase == null) {
          offlineRoomDatabase =
              Room.databaseBuilder(mApplication, OfflineRoomDatabase.class, "offline_database")
                  .build();
        }
      }
    }
  }

  @Singleton
  @Provides
  OfflineRoomDatabase providesRoomDatabase() {
    return offlineRoomDatabase;
  }

  @Provides
  CollectionStateDao providesCollectionStateDao(OfflineRoomDatabase demoDatabase) {
    return demoDatabase.collectionStateDao();
  }

  @Provides
  CollectionStateRepository providesCollectionStateRepository(
      @Global.NetworkIO Executor executor, CollectionStateDao collectionStateDao) {
    return new CollectionStateRepository(executor, collectionStateDao);
  }
}
