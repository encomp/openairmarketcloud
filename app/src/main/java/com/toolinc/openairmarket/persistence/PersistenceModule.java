package com.toolinc.openairmarket.persistence;

import com.toolinc.openairmarket.persistence.offline.room.OfflineDatabaseModule;

import javax.inject.Singleton;

import dagger.Module;

@Module(includes = {OfflineDatabaseModule.class})
public interface PersistenceModule {

  ProductBrandRepository providesProductBrandRepository();

  @Singleton
  ProductBrandOfflineRefresher providesProductBrandOfflineRefresher();
}
