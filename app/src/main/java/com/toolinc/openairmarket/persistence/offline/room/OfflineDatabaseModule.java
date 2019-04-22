package com.toolinc.openairmarket.persistence.offline.room;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/** Specifies the Offline database dagger module. */
@Module
public abstract class OfflineDatabaseModule {

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
  static OfflineRoomDatabase providesRoomDatabase() {
    return offlineRoomDatabase;
  }

  @Provides
  static CollectionStateDao providesCollectionStateDao(OfflineRoomDatabase demoDatabase) {
    return demoDatabase.collectionStateDao();
  }

  abstract CollectionStateRepository providesCollectionStateRepository();
}
